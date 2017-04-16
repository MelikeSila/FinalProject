package com.google.maps.android.utils.demo;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by Melike on 17.4.2017.
 */

public class FileUtil {
    public static File createImageFile() throws IOException {
        String milliSeconds = String.valueOf(System.currentTimeMillis());
        String imageFileName = "STORAGE_IMAGE_" + milliSeconds + "_";
        File storageDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, imageFileName + ".jpg");
        return image;
    }
}
