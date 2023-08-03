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

public class SearchAdapter extends FirestoreRecyclerAdapter<postDataModel, SearchAdapter.SearchHolder> {

    public SearchAdapter(@NonNull FirestoreRecyclerOptions<postDataModel> options) {
        super(options);
    }

    class SearchHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView price;
        TextView category;
        ImageView image;

        public SearchHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.postTitle);
            price = itemView.findViewById(R.id.postPrice);
            category = itemView.findViewById(R.id.postCategory);
            image = itemView.findViewById(R.id.imageFromDatabase);
        }
    }
    @Override
    protected void onBindViewHolder(@NonNull SearchHolder holder, int position, @NonNull postDataModel model) {
        holder.title.setText(model.getTitle());
        holder.price.setText("BDT " + String.valueOf(model.getPrice())+ " ৳");
        holder.category.setText(model.getCategory());
        Picasso.get().load(model.getImageUrl()).into(holder.image);
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.postitem,parent,false);
        return new SearchHolder(v);
    }


}
