package com.example.simpleinsta;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("UserImgs")
public class UserImgs extends ParseObject {
    public static final String KEY_IMAGE = "profilepic";
    public static final String KEY_USERNAME = "username";

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USERNAME);
    }

    public void setUser(ParseUser user) {
        put(KEY_USERNAME, user);
    }
}
