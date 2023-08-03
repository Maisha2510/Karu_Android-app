package com.example.karu_android_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class cart_3 extends AppCompatActivity {
private Button goHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.cart_part3);

            goHome = findViewById(R.id.backToHomeBTN);
            goHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), dashboard.class);
                    startActivity(intent);
                }
            });
    }
}
