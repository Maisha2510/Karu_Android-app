package com.example.karu_android_app;

import static android.service.controls.ControlsProviderService.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class userProfile extends AppCompatActivity {
    private ImageButton back;
    private ImageView profilepic;
    private Button editProfile;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore root = FirebaseFirestore.getInstance();
    private TextView uname, uphn, udob, uemail;
    public static final String Key_name = "name";
    public static final String Key_dob = "dob";
    public static final String Key_phn = "phone";
    public static final String Key_email = "email";
    String imageUrl_intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        back = (ImageButton) findViewById(R.id.backBTN);
        editProfile = findViewById(R.id.publish_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), editProfile.class);


                String name = uname.getText().toString();
                String phn = uphn.getText().toString();
                String email = uemail.getText().toString();
                String dob = udob.getText().toString();


                //intent pass data
                Bundle bundle = new Bundle();

                bundle.putString("name_PreIntent", name);
                bundle.putString("phn_PreIntent", phn);
                bundle.putString("email_PreIntent", email);
                bundle.putString("dob_PreIntent", dob);
                bundle.putString("propic_PreIntert", imageUrl_intent);

                intent.putExtras(bundle);

                startActivity(intent);

            }
        });

        uname = findViewById(R.id.userName);
        uemail = findViewById(R.id.uEmail);
        uphn = findViewById(R.id.uPhone);
        udob = findViewById(R.id.uDob);
        profilepic = findViewById(R.id.image_holder_cardView);

        DocumentReference docRef = root.collection("Users").document(user.getUid()).collection("basic_info").document("name");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = document.getString(Key_name);
                        String dob = document.getString(Key_dob);
                        String phn = document.getString(Key_phn);
                        String email = document.getString(Key_email);
                        String imageUrl = document.getString("userImage");
                        uname.setText(name);
                        uemail.setText(email);
                        udob.setText(dob);
                        uphn.setText(phn);
                        Picasso.get().load(imageUrl).into(profilepic);
                        imageUrl_intent = imageUrl;

                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }
}