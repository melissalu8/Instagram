package codepath.com.instagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

import codepath.com.instagram.Model.GlideApp;

import static android.app.Activity.RESULT_OK;

public class Profile extends Fragment {

    private Button logoutBtn;
    ImageButton ibAddProfile;
    ImageView ivProfilePicture;

    public final String APP_TAG = "Instagram";
    public static final int RESULT_LOAD_IMAGE = 0;
    public String photoFileName = "photo.jpg";
    public final int SOME_WIDTH = 30;
    public final int SOME_HEIGHT = 30;
    File photoFile;

    Bitmap resizedBitmap;
    ImageView ivPhotoUpload;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        logoutBtn = getActivity().findViewById(R.id.btnLogout);
        ibAddProfile = getActivity().findViewById(R.id.ibAddProfile);

        ivProfilePicture = getActivity().findViewById(R.id.ivProfilePic);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                final Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                Toast.makeText(getActivity().getApplicationContext(), "You have successfully logged out", Toast.LENGTH_LONG).show();
            }
        });

        ibAddProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Create a File reference to access to future access
                photoFile = getPhotoFileUri(photoFileName);

                Uri fileProvider = FileProvider.getUriForFile(getActivity(), "com.codepath.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                // So as long as the result is not null, it'bfkvekjieijdfbvvddkdnutnfeljlhcetullvnfeihinrfenrhtvfvcgbfhkhnkcdkflkfvrjlvtlhnctkhgivvubigknjctnnuubjfjlelvjhkuricdfglcchhiilgdrbhbihvllhinjbditvcjbfdjfghbujigikrekcgdvhgtgeejcjbvjtvttjgnhfgts safe to use the intent.
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Start the image capture intent to take photo
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                }
            }
        });

        populateProfilePicture();
        super.onViewCreated(view, savedInstanceState);
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }
    // When the camera app finishes, the onActivityResult() method will be called
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                resizedBitmap = Bitmap.createScaledBitmap(takenImage, SOME_WIDTH, SOME_HEIGHT, false);
                // Load the taken image into a preview
                ivPhotoUpload = (ImageView) getView().findViewById(R.id.ivProfilePic);
                populateProfilePicture();
                ivPhotoUpload.setImageBitmap(resizedBitmap);

                final File file = getPhotoFileUri(photoFileName);

                final ParseFile parseFile = new ParseFile(file);
                ParseUser currentUser = ParseUser.getCurrentUser();

                currentUser.put("profilePicture", parseFile);
                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            GlideApp.with(getActivity())
                                    .load(resizedBitmap)
                                    .centerCrop()
                                    .transform(new CircleCrop())
                                    .into(ivPhotoUpload);
                        } else {
                            e.printStackTrace();
                        }
                    }
                });

            } else { // Result was a failure
                Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void populateProfilePicture() {
        ParseUser currentUser = ParseUser.getCurrentUser();

        final ParseFile profilePicture = currentUser.getParseFile("profilePicture");
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    GlideApp.with(getActivity())
                            .load(profilePicture.getUrl())
                            .centerCrop()
                            .transform(new CircleCrop())
                            .into(ivProfilePicture);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
