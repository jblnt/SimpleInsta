package com.example.simpleinsta.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.simpleinsta.Camera;
import com.example.simpleinsta.Post;
import com.example.simpleinsta.R;
import com.example.simpleinsta.UserImgs;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Camera {
    public static final String TAG = "PostFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //User Defined --

    //UI elements
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Post logic
        ivPostImage = view.findViewById(R.id.ivPostImage);
        etDescription = view.findViewById(R.id.etDescription);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        ibPostCamera = view.findViewById(R.id.ibPostCamera);

        ibPostCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = "no_autosave";
                launchCamera();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                ParseUser currentUser =  ParseUser.getCurrentUser();

                ParseQuery<UserImgs> query = ParseQuery.getQuery(UserImgs.class);
                query.whereEqualTo(UserImgs.KEY_USER, currentUser);
                query.findInBackground(new FindCallback<UserImgs>() {
                    @Override
                    public void done(List<UserImgs> userImg, ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error Querying User Image", e);
                        } else {
                            savePost(description, currentUser, photoFile, userImg.get(0));
                        }
                    }
                });
            }
        });
    }

    // -- User methods
    private void savePost(String description, ParseUser user, File photo, UserImgs userImg) {
        Post post = new Post();

        post.setUserImg(userImg);

        //handle description
        if (description.isEmpty()){
            Toast.makeText(getContext(), "Description Required", Toast.LENGTH_SHORT).show();
            return;
        } else {
            post.setDescription(description.trim());
        }

        //handle user
        post.setUser(user);

        //error handling for photo
        if(photo == null || ivPostImage.getDrawable() == null){
            Toast.makeText(getContext(), "No Image Present", Toast.LENGTH_SHORT).show();
            return;
        }

        post.setImage(new ParseFile(photo));

        //Now save to backend
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    //Log.e(TAG, "error", e);
                    Toast.makeText(getContext(), "Error Posting...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Post Saved", Toast.LENGTH_SHORT).show();
                    etDescription.setText("");
                    ivPostImage.setImageResource(0);

                    //reDirectMainActivity();
                }
            }
        });
    }
}