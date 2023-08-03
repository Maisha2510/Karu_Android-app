package com.example.karu_android_app;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class searchPage_exhibition extends AppCompatActivity {
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore root = FirebaseFirestore.getInstance();
    private EditText searchBox;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference postReference = db.collection("Exhibition");
    private exhibitionPostAdapter adapter;
private ImageButton backBTN;

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "meet a IOOBE in RecyclerView");
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page_exhibition);


        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.search_recyclerView_exhibition);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        Query query = postReference.orderBy("eventName", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<exhibitionPostInfo> options = new FirestoreRecyclerOptions.Builder<exhibitionPostInfo>()
                .setQuery(query, exhibitionPostInfo.class)
                .build();


        // recyclerView.setHasFixedSize(true);


        adapter = new exhibitionPostAdapter(options);
        recyclerView.setAdapter(adapter);

        backBTN = findViewById(R.id.BackBTN);
        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchBox = findViewById(R.id.searchbox_exhibition);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("Searchbox has changed to: " + s.toString());
                Query query;
                if (s.toString().isEmpty()) {
                    query = postReference
                            .orderBy("price", Query.Direction.ASCENDING);
                } else {
                    query = postReference
                            .whereEqualTo("title", s.toString())
                            .orderBy("title", Query.Direction.ASCENDING);
                }
                FirestoreRecyclerOptions<exhibitionPostInfo> options = new FirestoreRecyclerOptions.Builder<exhibitionPostInfo>()
                        .setQuery(query, exhibitionPostInfo.class)
                        .build();
                adapter.updateOptions(options);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }

    }


}
