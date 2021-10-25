package com.example.simpleinsta.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.simpleinsta.Post;
import com.example.simpleinsta.R;
import com.example.simpleinsta.parseobjects.Likes;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private static final String TAG = "PostAdapter";

    private Context context;
    private List<Post> feed;

    public PostAdapter(Context context, List<Post> feed){
        this.context = context;
        this.feed = feed;
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
        Post post = feed.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return feed.size();
    }

    // -- Swipe Refresh methods
    public void clear() {
        feed.clear();
        notifyDataSetChanged();
    }

    public void addFresh(List<Post> list){
        feed.addAll(list);
        //notifyItemRangeInserted(0, posts.size());
        notifyDataSetChanged();
    }

    //Donno why this worked but fixed items being updated while
    //scrolling...
    /*
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    */

    // --
    //Define Viewholder to be used by Adapter
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivTimelimeProfilePic;
        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;
        private ImageView ivLikeButton;
        private TextView tvTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivTimelimeProfilePic = itemView.findViewById(R.id.ivTimelimeProfilePic);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);

            ivLikeButton = itemView.findViewById(R.id.ivLikeButton);
            ivLikeButton.setOnClickListener(this);

            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }

        public void bind(Post post) {
            Date postDate = post.getCreatedAt();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm dd.MMM.yyyy z", Locale.ENGLISH);

            //perform binding
            tvUsername.setText(post.getUser().getUsername());
            tvDescription.setText(post.getDescription());
            tvTimestamp.setText(simpleDateFormat.format(postDate));
            Glide.with(context).load(post.getImage().getUrl()).into(ivImage);
            Glide.with(context).load(post.getUserImg().getParseFile("profilepic").getUrl()).into(ivTimelimeProfilePic);
        }

        @Override
        public void onClick(View v) {
            int position = getAbsoluteAdapterPosition();
            if(position !=  RecyclerView.NO_POSITION) {
                Post p = feed.get(position);

                Toast.makeText(context, "Like Feature Coming Soon", Toast.LENGTH_SHORT).show();

                //likeButtonClicked(p);
            }
        }
    }

    private void likeButtonClicked(Post p) {
        Likes like = new Likes();

        like.setPost(p);
        like.setUser(ParseUser.getCurrentUser());

        like.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e(TAG, "Saving Like Error", e);
                }
            }
        });

    }
}
