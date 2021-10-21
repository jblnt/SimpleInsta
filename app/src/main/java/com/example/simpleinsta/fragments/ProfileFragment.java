package com.example.simpleinsta.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.simpleinsta.Camera;
import com.example.simpleinsta.Post;
import com.example.simpleinsta.R;
import com.example.simpleinsta.UserImgs;
import com.example.simpleinsta.adapters.GridAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Camera {
    public static final String TAG = "ProfileFragment";
    //ImageButton btnAccount;
    //File fileFromCam;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // --
        ParseUser parseUser = ParseUser.getCurrentUser();

        ivPostImage = view.findViewById(R.id.ivProfilePic);
        loadProfileImage(ivPostImage);
        ivPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = "autosave";
                launchCamera();
            }
        });

        TextView tvProfileName = view.findViewById(R.id.tvProfileName);
        tvProfileName.setText(ParseUser.getCurrentUser().getUsername());

        ImageView ibExit =  view.findViewById(R.id.ibExit);
        ibExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ParseUser.getCurrentUser() != null) {
                    ParseUser.logOut();
                }
            }
        });

        //User defined logic for timeline using RecyclerView;
        List<Post> posts = new ArrayList<>();
        GridAdapter adapter = new GridAdapter(getContext(), posts);

        RecyclerView rvTimeline = view.findViewById(R.id.rvProfileGrid);
        rvTimeline.setAdapter(adapter);
        rvTimeline.setLayoutManager(new GridLayoutManager(getContext(), 3));

        //Display Grid Information
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.setLimit(25);
        query.orderByDescending("createdAt");
        query.include("user.email");
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e != null){
                    //something went wrong
                    Log.e(TAG, "Query Went Wrong", e);
                    return;
                } else {
                    //Log.i(TAG, String.valueOf(objects.size()));
                    posts.addAll(objects);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void loadProfileImage(ImageView ivPostImage) {
        ParseQuery<UserImgs> query = ParseQuery.getQuery(UserImgs.class);
        query.whereEqualTo(UserImgs.KEY_USERNAME, ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<UserImgs>() {
            @Override
            public void done(List<UserImgs> objects, ParseException e) {
                for (UserImgs userimg: objects){
                    Glide.with(requireContext()).load(userimg.getImage().getUrl()).into(ivPostImage);
                }
            }
        });
    }

    //--user defined
    /*
    protected void savePost(File photoFile) {
        ParseQuery<UserImgs> query = ParseQuery.getQuery(UserImgs.class);
        query.whereEqualTo(UserImgs.KEY_USERNAME, ParseUser.getCurrentUser());

        query.findInBackground(new FindCallback<UserImgs>() {
            @Override
            public void done(List<UserImgs> objects, ParseException e) {
                for (UserImgs userimg: objects){
                    userimg.put(UserImgs.KEY_IMAGE, new ParseFile(photoFile));
                    userimg.saveInBackground();
                }
            }
        });

        UserImgs userImgs = new UserImgs();

        userImgs.setImage(new ParseFile(photoFile));
        userImgs.setUser(currentUser);

        if(photoFile == null || ivPostImage.getDrawable() == null){
            Toast.makeText(getContext(), "No Image Present", Toast.LENGTH_SHORT).show();
            return;
        }

        userImgs.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "error", e);
                    Toast.makeText(getContext(), "Error Posting...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Post Saved", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void logOutUser() {
        if(ParseUser.getCurrentUser() != null){
            ParseUser.logOut();

            //If you made it here, it means we can launch the main timeline;
            //reDirectLoginActivity();
            //finish();
        }

    }

    private void reDirectLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);

        //Finishes the loginActivity and removed it from the activity stack;
        finish();
    }
    */
}