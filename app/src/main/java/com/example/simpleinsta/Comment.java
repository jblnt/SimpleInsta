package com.example.simpleinsta;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comments")
public class Comment extends ParseObject {
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_POST = "post";
    public static final String KEY_EMAIL = "email";

    public String getComment() {
        return getString(KEY_COMMENT);
    }

    public void setComment(String comment){
        put(KEY_COMMENT, comment);
    }

    public ParseObject getPost() {
        return getParseObject(KEY_POST);
    }

    public void setPost(ParseObject post){
        put(KEY_POST, post);
    }

    public ParseUser getEmail(){
        return getParseUser(KEY_EMAIL);
    }

    public void setEmail(ParseUser email){
        put(KEY_EMAIL, email);
    }
}
