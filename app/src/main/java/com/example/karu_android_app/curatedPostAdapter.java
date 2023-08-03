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

public class curatedPostAdapter extends FirestoreRecyclerAdapter<postDataModel, curatedPostAdapter.curatedPostHolder> {

    public curatedPostAdapter(@NonNull FirestoreRecyclerOptions<postDataModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull curatedPostHolder holder, int position, @NonNull postDataModel model) {
        holder.title.setText(model.getTitle());
        Picasso.get().load(model.getImageUrl()).into(holder.image);
    }

    @NonNull
    @Override
    public curatedPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.curated_post_sample,parent,false);
        return new curatedPostHolder(v);
    }

    class curatedPostHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView image;

        public curatedPostHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.curated_postTitle);
            image = itemView.findViewById(R.id.curated_imageFromDatabase);
        }
    }
}
