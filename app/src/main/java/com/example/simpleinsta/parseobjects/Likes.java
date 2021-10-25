package com.example.simpleinsta.parseobjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Likes")
public class Likes extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_POST = "post";

    public ParseUser getUser(){ return getParseUser(KEY_USER); }
    public void setUser(ParseUser user){ put(KEY_USER, user); }

    public ParseObject getPost(){ return getParseObject(KEY_POST); }
    public void setPost(ParseObject post){ put(KEY_POST, post); }
}
