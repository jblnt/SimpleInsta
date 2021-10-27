package com.example.simpleinsta;

import android.util.Log;

import com.example.simpleinsta.parseobjects.Likes;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class Like {
    public static final String TAG = "LIKE";
    private Post post;
    private ParseUser user;
    private String status = "false";

    public boolean getLikeStatus(Post post, ParseUser user){
        ParseQuery<Likes> query = ParseQuery.getQuery(Likes.class);
        query.setLimit(20);
        query.whereEqualTo(Likes.KEY_USER, user);
        query.whereEqualTo(Likes.KEY_POST, post);

        try {
            query.getFirst();
            //Log.i(TAG, query.getFirst().getPost().getObjectId());
            status = "true";
        } catch (ParseException e) {
            //Log.e(TAG, "null like returned", e);
        }
        return status.equals("true");
    }
}
