package com.google.maps.android.utils.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;

public class CreateGameActivity extends Activity implements View.OnClickListener {
    private Button nextButton1;
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        nextButton1 = (Button) findViewById(R.id.nextButtton1);
        nextButton1.setOnClickListener(this);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nextButtton1:
                openObjProperty();
                break;
        }
    }

    private void openObjProperty(){
       Intent i = new Intent(getApplicationContext(),ObjPropertyActivity.class);
       startActivity(i);
    }
}
