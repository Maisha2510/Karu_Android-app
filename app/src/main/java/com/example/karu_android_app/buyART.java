package com.example.karu_android_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.HashMap;
import java.util.Map;

public class buyART extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference postReference = db.collection("Posts");

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.d1, R.drawable.d2, R.drawable.d3, R.drawable.d4, R.drawable.d5};

    private Button search_button;
    private ImageButton back,cart;
    FirestoreRecyclerAdapter<postDataModel, postHolder> recyclerAdapter;
    FirestoreRecyclerAdapter<postDataModel, curatedPostHolder> recyclerAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_art);

        back = findViewById(R.id.BackBTN);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

       cart = findViewById(R.id.cartBTN);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),main_cart.class);
                startActivity(intent);
            }
        });
        search_button = findViewById(R.id.searchBTN);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), search_page.class);
                startActivity(intent);

            }
        });

        /// carousel components ////////////////////////////////////////
        carouselView = (CarouselView) findViewById(R.id.carouselImage);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

        Query query = postReference.orderBy("price", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<postDataModel> allinfo = new FirestoreRecyclerOptions.Builder<postDataModel>().setQuery(query, postDataModel.class).build();
        recyclerAdapter = new FirestoreRecyclerAdapter<postDataModel, postHolder>(allinfo) {
            @Override
            protected void onBindViewHolder(@NonNull postHolder holder, int position, @NonNull postDataModel model) {

                String docId = recyclerAdapter.getSnapshots().getSnapshot(position).getId();
                holder.title.setText(model.getTitle());
                holder.price.setText("BDT " + String.valueOf(model.getPrice()) + " ৳");
                holder.category.setText(model.getCategory());
                Picasso.get().load(model.getImageUrl()).into(holder.image);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), postDetails.class);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("price", String.valueOf(model.getPrice()));
                        intent.putExtra("image_uri", model.getImageUrl());
                        intent.putExtra("description", model.getDescription());
                        startActivity(intent);
                    }
                });

                holder.fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> postInfo = new HashMap<>();
                        postInfo.put("title",model.getTitle());
                        postInfo.put("size",model.getSize());
                        postInfo.put("price",model.getPrice());
                        postInfo.put("imageUrl",model.getImageUrl());
                        postInfo.put("description",model.getDescription());
                        postInfo.put("category",model.getCategory());


                        DocumentReference documentReference = db.collection("Users").document(user.getUid()).collection("Favourite").document(model.getTitle());
                        documentReference.set(postInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();

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

            @NonNull
            @Override
            public postHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.postitem, parent, false);
                return new postHolder(view);
            }
        };

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setItemAnimator(null);
        curatedPostInitiate();

    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };


    private void curatedPostInitiate() {
        Query query = postReference.orderBy("title", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<postDataModel> allinfo = new FirestoreRecyclerOptions.Builder<postDataModel>().setQuery(query, postDataModel.class).build();
        recyclerAdapter2 = new FirestoreRecyclerAdapter<postDataModel, curatedPostHolder>(allinfo) {
            @Override
            protected void onBindViewHolder(@NonNull curatedPostHolder holder, int position, @NonNull postDataModel model) {

                String docId = recyclerAdapter2.getSnapshots().getSnapshot(position).getId();
                holder.title.setText(model.getTitle());
                Picasso.get().load(model.getImageUrl()).into(holder.image);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), postDetails.class);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("price", String.valueOf(model.getPrice()));
                        intent.putExtra("image_uri", model.getImageUrl());
                        intent.putExtra("description", model.getDescription());
                        startActivity(intent);
                    }
                });


            }

            @NonNull
            @Override
            public curatedPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.curated_post_sample, parent, false);
                return new curatedPostHolder(view);
            }
        };
        class WrapContentLinearLayoutManager extends LinearLayoutManager {
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
        RecyclerView recyclerView = findViewById(R.id.curatedRecycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(recyclerAdapter2);
        recyclerView.setItemAnimator(null);
    }

    public class postHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView price;
        TextView category;
        ImageView image;
        ImageButton fav;

        public postHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.postTitle);
            price = itemView.findViewById(R.id.postPrice);
            category = itemView.findViewById(R.id.postCategory);
            image = itemView.findViewById(R.id.imageFromDatabase);
            fav = itemView.findViewById(R.id.favBTN);

        }
    }

//// for experiment
    public class curatedPostHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView price;
        TextView category;
        ImageView image;

        public curatedPostHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.curated_postTitle);
            image = itemView.findViewById(R.id.curated_imageFromDatabase);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
        recyclerAdapter2.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
        recyclerAdapter2.stopListening();
    }
}