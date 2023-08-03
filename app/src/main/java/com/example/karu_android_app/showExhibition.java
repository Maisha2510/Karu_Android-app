package com.example.karu_android_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class showExhibition extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference postReference = db.collection("Exhibition");

    private exhibitionPostAdapter adapter;
    private FloatingActionButton floatingBTN;
    private ImageButton back;
    private Button search_button;

    FirestoreRecyclerAdapter<exhibitionPostInfo, postHolder> recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_exhibition);

        back = findViewById(R.id.BackBTN);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        search_button = findViewById(R.id.searchBTN);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), searchPage_exhibition.class);
                startActivity(intent);
            }
        });
        floatingBTN = findViewById(R.id.plusBTN);
        floatingBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), post_exhibition.class);
                startActivity(intent);
            }
        });

        Query query = postReference.orderBy("eventDate", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<exhibitionPostInfo> allInfo = new FirestoreRecyclerOptions.Builder<exhibitionPostInfo>().setQuery(query, exhibitionPostInfo.class).build();
        recyclerAdapter = new FirestoreRecyclerAdapter<exhibitionPostInfo, postHolder>(allInfo) {
            @Override
            protected void onBindViewHolder(@NonNull postHolder holder, int position, @NonNull exhibitionPostInfo model) {

                String docId = recyclerAdapter.getSnapshots().getSnapshot(position).getId();
                holder.event_name.setText(model.getEventName());
                holder.event_date_place.setText(model.getEventPlace() + ", " + model.getEventDate());
                holder.event_price.setText(String.valueOf("BDT " + model.getTicketPrice() + " ৳"));
                holder.event_hostName.setText("Hosted by " + model.getEventHost());
                Picasso.get().load(model.getEventLogo()).into(holder.event_logo);
                holder.purchaseBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("exhibition clicked");

                        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                                showExhibition.this, R.style.BottomSheetDialogTheme);
                        View bottomSheetView = LayoutInflater.from(showExhibition.this)
                                .inflate(R.layout.bottom_sheet_exhibition_purchase,
                                        (LinearLayout) findViewById(R.id.bottomSheetContainer));
                        bottomSheetView.findViewById(R.id.confirmPurchase).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                System.out.println("purchased TIcket");
                                bottomSheetDialog.dismiss();
                            }
                        });
                        TextView event_name, event_host, price;
                        event_name = bottomSheetView.findViewById(R.id.eventNameInBS);
                        event_host = bottomSheetView.findViewById(R.id.eventHostInBS);
                        price = bottomSheetView.findViewById(R.id.eventPriceInBS);

                        event_name.setText(model.getEventName());
                        event_host.setText(model.getEventHost());
                        price.setText(String.valueOf(model.getTicketPrice()));


                        bottomSheetDialog.setContentView(bottomSheetView);
                        bottomSheetDialog.show();

                    }
                });
            }

            @NonNull
            @Override
            public postHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.activity_sample_exhibition, parent, false);
                return new postHolder(view);
            }
        };
        RecyclerView recyclerView = findViewById(R.id.Exhibition_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);


        //  setUpRecyclerView();
    }

    public class postHolder extends RecyclerView.ViewHolder {
        TextView event_name;
        TextView event_date_place;
        TextView event_price;
        TextView event_hostName;
        ImageView event_logo;
        ImageButton purchaseBTN;

        public postHolder(@NonNull View itemView) {
            super(itemView);
            event_name = itemView.findViewById(R.id.eventNameText);
            event_date_place = itemView.findViewById(R.id.eventPlaceText);
            event_price = itemView.findViewById(R.id.ticketPrice);
            event_hostName = itemView.findViewById(R.id.hostName);
            event_logo = itemView.findViewById(R.id.image_holder_cardView);
            purchaseBTN = itemView.findViewById(R.id.buy_ticket);
        }
    }
//    private void setUpRecyclerView() {
//        Query query = postReference.orderBy("eventDate", Query.Direction.ASCENDING);
//        FirestoreRecyclerOptions<exhibitionPostInfo> options = new FirestoreRecyclerOptions.Builder<exhibitionPostInfo>()
//                .setQuery(query, exhibitionPostInfo.class)
//                .build();
//        adapter = new exhibitionPostAdapter(options);
//        RecyclerView recyclerView = findViewById(R.id.Exhibition_recycler_view);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//    }

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