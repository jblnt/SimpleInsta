package com.example.simpleinsta;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Comments")
public class Comment extends ParseObject {
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_POST = "post";
    public static final String KEY_EMAIL = "email";


}
