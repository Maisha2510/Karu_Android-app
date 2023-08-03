package com.example.karu_android_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class exhibitionPostAdapter extends FirestoreRecyclerAdapter<exhibitionPostInfo, exhibitionPostAdapter.exhibitionHolder> {

    public exhibitionPostAdapter(@NonNull FirestoreRecyclerOptions<exhibitionPostInfo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull exhibitionHolder holder, int position, @NonNull exhibitionPostInfo model) {
        holder.event_name.setText(model.getEventName());
         holder.event_date_place.setText(model.getEventPlace()+", "+ model.getEventDate());
         holder.event_price.setText(String.valueOf("BDT " +model.getTicketPrice()+ " ৳"));
         holder.event_hostName.setText("Hosted by "+ model.getEventHost());
        Picasso.get().load(model.getEventLogo()).into(holder.event_logo);

    }

    @NonNull
    @Override
    public exhibitionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_sample_exhibition, parent, false);
        return new exhibitionHolder(v);
    }


    class exhibitionHolder extends RecyclerView.ViewHolder {
        TextView event_name;
        TextView event_date_place;
        TextView event_price;
        TextView event_hostName;
        ImageView event_logo;

        public exhibitionHolder(@NonNull View itemView) {
            super(itemView);
            event_name = itemView.findViewById(R.id.eventNameText);
            event_date_place = itemView.findViewById(R.id.eventPlaceText);
            event_price = itemView.findViewById(R.id.ticketPrice);
            event_hostName = itemView.findViewById(R.id.hostName);
            event_logo = itemView.findViewById(R.id.image_holder_cardView);
        }
    }
}
