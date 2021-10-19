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

import com.example.simpleinsta.MainActivity;
import com.example.simpleinsta.Post;
import com.example.simpleinsta.PostActivity;
import com.example.simpleinsta.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {
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
    private ImageView ivPostImage;
    private EditText etDescription;
    private Button btnSubmit;
    //private Button btnCamera;
    private ImageButton ibPostCamera;

    //Bitmap Saving
    public static final int CAPTURE_IMAGE_REQUEST_CODE = 23;
    private File photoFile;
    private final String photoFileName = "capture.jpg";
    private final String APP_TAG = "SimpleInsta";
    private Object BitmapScaler;

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

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                ParseUser currentUser =  ParseUser.getCurrentUser();

                savePost(description,currentUser, photoFile);
            }
        });

        ibPostCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });
    }

    //user methods
    private void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();

        //handle description
        if (description.isEmpty()){
            Toast.makeText(getContext(), "Description Needed!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            post.setDescription(description);
        }

        //handle user
        post.setUser(currentUser);

        //handle photo
        if(photoFile == null || ivPostImage.getDrawable() == null){
            Toast.makeText(getContext(), "No Image Present", Toast.LENGTH_SHORT).show();
            return;
        }

        post.setImage(new ParseFile(photoFile));

        //Now save to backend
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "error", e);
                    Toast.makeText(getContext(), "Error Posting...", Toast.LENGTH_SHORT).show();
                } else {
                    //Log.i(TAG, "Post Saved...");
                    Toast.makeText(getContext(), "Post Saved", Toast.LENGTH_SHORT).show();
                    etDescription.setText("");
                    ivPostImage.setImageResource(0);

                    //reDirectMainActivity();
                }
            }
        });
    }

    /*
    private void reDirectMainActivity() {
        Intent i = new Intent(getContext(), MainActivity.class);
        startActivity(i);

        //finish();
    }
    */

    private void launchCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(requireContext(), "com.simpleinsta.fileprovider", photoFile);

        i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if(i.resolveActivity(requireContext().getPackageManager()) != null){
            startActivityForResult(i, CAPTURE_IMAGE_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURE_IMAGE_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                ivPostImage.setImageBitmap(takenImage);
            } else {
                Toast.makeText(getContext(), "No Image Taken", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File getPhotoFileUri(String photoFileName) {
        File mediaStorgeDir = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        if(!mediaStorgeDir.exists() && !mediaStorgeDir.mkdirs()){
            Log.d(TAG, "Failed to Create Directory");
        }

        return new File(mediaStorgeDir.getPath() + File.separator + photoFileName);
    }

}