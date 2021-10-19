package com.example.simpleinsta;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;

    private TextView tvUsername;
    private ImageView ivImage;
    private TextView tvDescription;

    public PostAdapter (Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    //Handle binding the data to the ViewHolder from datasource.
    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        //Get a post at position and then bind the data;
        Post post = posts.get(position);

        //perform binding
        tvUsername.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());

        Glide.with(context).load(post.getImage().getUrl()).into(ivImage);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // --

    //Define Viewholder to be used by Adapter
    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}
