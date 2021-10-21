package com.example.simpleinsta;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;

public class Camera extends Fragment {
    protected static final String TAG = "Camera";

    protected ImageView ivPostImage;
    protected EditText etDescription;
    protected Button btnSubmit;
    //private Button btnCamera;
    protected ImageButton ibPostCamera;

    //Bitmap Saving
    public static final int CAPTURE_IMAGE_REQUEST_CODE = 23;
    protected File photoFile;
    protected final String photoFileName = "capture.jpg";
    protected final String APP_TAG = "SimpleInsta";

    //user methods
    /*
    private void reDirectMainActivity() {
        Intent i = new Intent(getContext(), MainActivity.class);
        startActivity(i);

        //finish();
    }
    */
    protected void launchCamera() {
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

                //ParseUser currentUser = ParseUser.getCurrentUser();
                //savePost("", currentUser, photoFile);

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

    /*
    protected void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();

        //handle description
        if (description.isEmpty()){
            Toast.makeText(getContext(), "Description Needed!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            post.setDescription(description.trim());
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
    */
}
