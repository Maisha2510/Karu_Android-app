package com.example.karu_android_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.HashMap;
import java.util.Map;

public class postDetails extends AppCompatActivity {

    TextView title_view, price_view, description_view;
    ImageView image_view;
    Button addCart;
    ImageButton cart;
    private ImageButton back;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore root = FirebaseFirestore.getInstance();

    //dtabase a save hobe ai nam a
    public static final String Key_name = "title";
    public static final String Key_price = "price";
    public static final String Key_image = "imageUrl";
    public static final String Key_count = "count";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        back = findViewById(R.id.backBTN);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.number_picker);
        numberPicker.setMax(15);
        numberPicker.setMin(0);
        numberPicker.setUnit(1);
        numberPicker.setValue(1);
        //   double count = Double.parseDouble(String.valueOf(numberPicker.getValue()));
        //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        Intent data = getIntent();
        String title = data.getStringExtra("title");
        String price = data.getStringExtra("price");
        String description = data.getStringExtra("description");
        String image_uri = data.getStringExtra("image_uri");

        title_view = findViewById(R.id.title_details);
        price_view = findViewById(R.id.details_price);
        description_view = findViewById(R.id.description_details);
        image_view = findViewById(R.id.imageViewDetails);
        addCart = findViewById(R.id.addCartBTN);


        title_view.setText(title);
        price_view.setText("BDT " + price + " ৳");
        description_view.setText(description);
        Picasso.get().load(image_uri).into(image_view);

        cart = findViewById(R.id.cartBTN);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), main_cart.class);
                startActivity(intent);
            }
        });

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // System.out.println((double) numberPicker.getValue());

                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put(Key_name, title);
                userInfo.put(Key_price, Double.valueOf(price));
                userInfo.put(Key_image, image_uri);
                userInfo.put(Key_count, numberPicker.getValue());


                DocumentReference documentReference = root.collection("Users").document(user.getUid()).collection("cart").document(title);
                documentReference.set(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Added successfully", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }
}

