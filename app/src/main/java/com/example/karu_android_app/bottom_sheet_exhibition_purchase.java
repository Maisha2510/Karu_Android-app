package com.example.karu_android_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class bottom_sheet_exhibition_purchase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);


        Intent data = getIntent();
        String title = data.getStringExtra("title");
        String price = data.getStringExtra("price");
        String hostName = data.getStringExtra("hostName");



    }


}
