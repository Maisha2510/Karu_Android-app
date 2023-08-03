package com.example.karu_android_app;


import static android.service.controls.ControlsProviderService.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class homeFragment extends Fragment {
 private CardView sellArtFunction, buyArtFunction, exhibitionFunction, newsFunction;
 private ImageButton cart,fav;
    private TextView user_name;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore root = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        sellArtFunction = v.findViewById(R.id.cardView2);
        buyArtFunction = v.findViewById(R.id.cardView1);
        exhibitionFunction = v.findViewById(R.id.cardView3);
        cart=v.findViewById(R.id.cartBTN);
        fav = v.findViewById(R.id.favBtn);
        cart = v.findViewById(R.id.cartBTN);
        user_name = v.findViewById(R.id.user_name_display);
newsFunction = v.findViewById(R.id.cardView4);
newsFunction.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), news_webview.class);
        startActivity(intent);
    }
});
        DocumentReference docRef = root.collection("Users").document(user.getUid()).collection("basic_info").document("name");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = document.getString("name");
                        user_name.setText(name);

                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        sellArtFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), sellArt.class);
                startActivity(intent);

            }
        });

        buyArtFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), buyART.class);
                startActivity(intent);

            }
        });
        exhibitionFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), showExhibition.class);
                startActivity(intent);

            }
        });

        ////////////////////////////        cart if else            //////////////////////////////
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), main_cart.class);
                startActivity(intent);

            }
        });
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), favourite.class);
                startActivity(intent);
            }
        });

        return v;
    }
}