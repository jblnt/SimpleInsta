package com.example.simpleinsta;

import android.app.Application;

import com.example.simpleinsta.parseobjects.Likes;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(UserImgs.class);
        ParseObject.registerSubclass(Likes.class);

        //Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(this)
            .applicationId("cDHeLOaLhDE6oOGcclwa4ViHdqc2M3MsHUb2fy2g")
            .clientKey("mrNXyJdCsFWQPzPuqOkvzFaNg4I2lHL7rkz4pgJr")
            .server("https://parseapi.back4app.com/")
            .build()
        );
    }
}
