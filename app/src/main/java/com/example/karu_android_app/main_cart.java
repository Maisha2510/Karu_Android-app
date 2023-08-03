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
import android.widget.EditText;
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
import com.travijuu.numberpicker.library.NumberPicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class main_cart extends AppCompatActivity {
    private ImageButton back;
    private Button browse;
    private Button placeOrderButton;
    private TextView subTotal, Total_price;
    private EditText deliveryAddress;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference cartReference = db.collection("Users").document(user.getUid()).collection("cart"); //////////////////////////////
    FirestoreRecyclerAdapter<cartDataModel, cartHolder> recyclerAdapter;

    Double totalPrice = 0.0;
    String orders = "(Order in sequential manner) ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cart);
        subTotal = findViewById(R.id.subtotalPrice);
        deliveryAddress = findViewById(R.id.delivery_address);
        back = findViewById(R.id.backBTN);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        placeOrderButton = findViewById(R.id.placeOrderBTN);

        Total_price = findViewById(R.id.totalPrice_text);

     /*   browse = findViewById(R.id.browseBTN);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),main_cart.class);
                startActivity(intent);
            }
        });*/
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


        Query query = cartReference.orderBy("price", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<cartDataModel> allinfo = new FirestoreRecyclerOptions.Builder<cartDataModel>().setQuery(query, cartDataModel.class).build();
        recyclerAdapter = new FirestoreRecyclerAdapter<cartDataModel, cartHolder>(allinfo) {
            @Override
            protected void onBindViewHolder(@NonNull cartHolder holder, int position, @NonNull cartDataModel model) {

                String docId = recyclerAdapter.getSnapshots().getSnapshot(position).getId();
                holder.title.setText(model.getTitle());
                holder.price.setText("BDT " + String.valueOf(model.getPrice()) + " ৳");
                Picasso.get().load(model.getImageUrl()).into(holder.image);
                totalPrice = totalPrice + (model.getPrice() * (double) model.getCount());
                subTotal.setText(totalPrice.toString());
                double finalPrice = totalPrice + 60.0;
                Total_price.setText(String.valueOf(finalPrice));
                holder.numberPicker.setValue(model.getCount());
                holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.collection("Users").document(user.getUid()).collection("cart").document(model.getTitle())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        System.out.println("cart theke delete hobe"+ model.getTitle());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        System.out.println("cart theke delete hocche naaaaaaaaaaaaaaaaaaaaa");
                                    }
                                });
                    }
                });
                orders = orders + " : " + model.getTitle();

               /* holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), postDetails.class);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("price", String.valueOf(model.getPrice()));
                        intent.putExtra("image_uri", model.getImageUrl());

                        startActivity(intent);
                    }
                });*/


                placeOrderButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "Order Placed Successfully", Toast.LENGTH_SHORT).show();


                        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                        Map<String, Object> transactionInfo = new HashMap<>();
                        transactionInfo.put("order_list", orders);
                        transactionInfo.put("total_price", finalPrice);
                        transactionInfo.put("delivery_address", deliveryAddress.getText().toString());
                        //transactionInfo.put("time_date", date);


                        DocumentReference documentReference = db.collection("Users").document(user.getUid()).collection("Transactions").document();
                        documentReference.set(transactionInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                               // Toast.makeText(getApplicationContext(), "Transaction e add hoise. Sabash!!", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Toast.makeText(getApplicationContext(), "Check Transaction for more details", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(),cart_3.class);
                        startActivity(intent);
                    }
                });
            }


            @NonNull
            @Override
            public cartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.activity_sample_cart, parent, false);
                return new cartHolder(view);
            }


        };
        RecyclerView recyclerView = findViewById(R.id.Cart_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
        subTotal.setText(totalPrice.toString());

    }

    public class cartHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView price;
        ImageView image;
        NumberPicker numberPicker;
        ImageButton deleteButton;

        public cartHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.cart_postTitle);
            price = itemView.findViewById(R.id.cart_postPrice);
            image = itemView.findViewById(R.id.cart_imageFromDatabase);
            numberPicker = itemView.findViewById(R.id.number_picker_forCart);
            deleteButton = itemView.findViewById(R.id.deleteBtn);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
    }
}



