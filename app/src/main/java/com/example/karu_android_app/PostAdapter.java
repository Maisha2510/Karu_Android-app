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

public class PostAdapter extends FirestoreRecyclerAdapter<postDataModel, PostAdapter.PostHolder> {

    public PostAdapter(@NonNull FirestoreRecyclerOptions<postDataModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PostHolder holder, int position, @NonNull postDataModel model) {



    holder.title.setText(model.getTitle());
    holder.price.setText("BDT " + String.valueOf(model.getPrice())+ " ৳");
    holder.category.setText(model.getCategory());
    Picasso.get().load(model.getImageUrl()).into(holder.image);
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.postitem,parent,false);
        return new PostHolder(v);
    }

    class PostHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView price;
        TextView category;
        ImageView image;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.postTitle);
            price = itemView.findViewById(R.id.postPrice);
            category = itemView.findViewById(R.id.postCategory);
            image = itemView.findViewById(R.id.imageFromDatabase);
        }
    }
}