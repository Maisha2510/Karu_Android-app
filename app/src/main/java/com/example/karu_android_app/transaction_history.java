package com.example.karu_android_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class transaction_history extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference postReference = db.collection("Users").document(user.getUid()).collection("Transactions");
    FirestoreRecyclerAdapter< transactionDataModel, postHolder> recyclerAdapter;
private ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

backButton = findViewById(R.id.backBTN);
backButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
            finish();
    }
});
        Query query = postReference.orderBy("total_price", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions< transactionDataModel> allinfo = new FirestoreRecyclerOptions.Builder< transactionDataModel>().setQuery(query,transactionDataModel.class).build();
        recyclerAdapter = new FirestoreRecyclerAdapter< transactionDataModel, postHolder>(allinfo) {
            @Override
            protected void onBindViewHolder(@NonNull  postHolder holder, int position, @NonNull  transactionDataModel model) {

                String docId = recyclerAdapter.getSnapshots().getSnapshot(position).getId();
                holder.order_list.setText(model.getOrder_list());
                holder.total_spend.setText("BDT " + String.valueOf(model.getTotal_price()) + " ৳");
                holder.address.setText("Deliverd to: " + model.getDelivery_address());
                //holder.date.setText(model.getTime_date());

            }

            @NonNull
            @Override
            public postHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.transaction_sample, parent, false);
                return new postHolder(view);
            }
        };
        RecyclerView recyclerView = findViewById(R.id.transactionRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);


    }

    public class postHolder extends RecyclerView.ViewHolder {
        TextView total_spend;
        TextView date;
        TextView address;
        EditText order_list;

        public postHolder(@NonNull View itemView) {
            super(itemView);
            total_spend = itemView.findViewById(R.id.totalSpend);
            date = itemView.findViewById(R.id.date_view);
            order_list = itemView.findViewById(R.id.orderList_view);
            address = itemView.findViewById(R.id.address_view);
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