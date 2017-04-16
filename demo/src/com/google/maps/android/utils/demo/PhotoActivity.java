package com.google.maps.android.utils.demo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class PhotoActivity extends Activity {
    private Uri selectedImage=null;
    private View imageContainer;
    private TextView overlayText;
    private ProgressBar progressBar;
    private Button uploadButton;

    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    // Get a non-default Storage bucket
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        imageContainer = findViewById(R.id.imgView);
        //overlayText.setText("");
        //overlayText.setVisibility(View.INVISIBLE);
        EditText textInput = (EditText)findViewById(R.id.imageText);
        //textInput.addTextChangedListener(new InputTextWatcher());
        //downloadUrl = (TextView) findViewById(R.id.download_url)
        uploadButton = (Button) findViewById(R.id.buttonSave);
        //progressBar = (ProgressBar) findViewById(R.id.progress_bar) ;
        //progressBar.setVisibility(View.GONE);
        System.out.println("onCreate");;
    }

    /*private class InputTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after){

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            overlayText.setVisibility(s.length() > 0 ? View.VISIBLE : View.INVISIBLE );
            overlayText.setText(s.toString());
        }
    }*/

    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        System.out.println("loadImageFromGallery");;
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    public void addPhotoToMap (View view){
        imageContainer.setDrawingCacheEnabled(true);
        imageContainer.buildDrawingCache();
        Bitmap bitmap = imageContainer.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        imageContainer.setDrawingCacheEnabled(false);
        byte[] data = baos.toByteArray();

        System.out.println("addPhotoToMap");;
        String path = "firememes/" + UUID.randomUUID() + ".png";
        StorageReference firememeRef = storage.getReference(path);

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCustomMetadata("text", "overlayText.getText().toString()")
                .build();
        //progressBar.setVisibility(View.VISIBLE);
        uploadButton.setVisibility(View.VISIBLE);

        UploadTask uploadTask = firememeRef.putBytes(data, metadata);
        uploadTask.addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               //progressBar.setVisibility(View.GONE);
                uploadButton.setEnabled(true);
            }
        });
        //fotograf var mı load edildi mi kontrol edicem
        //fotoğraf varsa map'e eklicem
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
                System.out.println("onACtivityResult");
                selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                    ImageView imgView = (ImageView) findViewById(R.id.imgView);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
///////////////////////////////////////////////
/*
        Uri pickedImage = data.getData();
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
        cursor.moveToFirst();
        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
        cursor.close();

        Uri file = Uri.fromFile(new File(imagePath));
        StorageReference storageRef = null;
        StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
        UploadTask uploadTask;
        uploadTask = riversRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });*/
    }
}
