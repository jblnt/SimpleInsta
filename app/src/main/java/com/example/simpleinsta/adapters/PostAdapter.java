package com.example.simpleinsta.adapters;

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
import com.example.simpleinsta.Post;
import com.example.simpleinsta.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private static final String TAG = "PostAdapter";
    private Context context;
    private List<Post> posts;

    private ImageView ivTimelimeProfilePic;
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
        Log.i(TAG, String.valueOf(position) + "|" +posts.get(position).getDescription());
        Post post = posts.get(position);

        //perform binding
        tvUsername.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());
        Glide.with(context).load(post.getImage().getUrl()).into(ivImage);
        Glide.with(context).load(post.getUserImg().getParseFile("profilepic").getUrl()).into(ivTimelimeProfilePic);
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

            ivTimelimeProfilePic = itemView.findViewById(R.id.ivTimelimeProfilePic);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }

    //Swipe Refresh methods
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> list){
        posts.addAll(list);
        notifyDataSetChanged();
    }
}
