package example.android.com.roadsuntaken;

import android.location.Location;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.opengles.GL;

import example.android.com.roadsuntaken.models.Post;
import example.android.com.roadsuntaken.models.User;

/**
 * Created by Shivam Garg on 24-03-2017.
 */

public class NewPlaceActivity extends BaseActivity {

    private static final String TAG = "NewPostActivity";

    private EditText mTitle, mHowToReach, mExperience;
    private double mLatitude, mLongitude;
    private FloatingActionButton mSubmitButton;
    private MyLocation mLocation = new MyLocation();
    private DatabaseReference mDatabase;
    private ImageView loading;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_place);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        mTitle = (EditText) findViewById(R.id.title);
        mHowToReach = (EditText) findViewById(R.id.how_to_reach_there);
        mExperience = (EditText) findViewById(R.id.experience);
        mSubmitButton = (FloatingActionButton) findViewById(R.id.fab_new_post);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });

        mSubmitButton.setEnabled(false);
        loading = (ImageView) findViewById(R.id.location_image);

        Glide.with(this)
                .load(R.drawable.loading)
                .into(loading);

        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                setImage(location.getLatitude(), location.getLongitude());
                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
                mSubmitButton.setEnabled(true);
            }
        };
        mLocation.getLocation(this, locationResult);

    }

    private void setImage(double latitude, double longitude) {
        ImageView image = (ImageView) findViewById(R.id.location_image);
        Glide.with(this)
                .load(getGoogleMapThumbnailUrl(latitude, longitude))
                .into(image);
    }

    private void submitPost() {
        final String title = mTitle.getText().toString();
        final String howToReach = mHowToReach.getText().toString();
        final String experience = mExperience.getText().toString();

        final String userId = getUid();

        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if (user == null) {
                            Toast.makeText(NewPlaceActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else if (title.length() == 0 || howToReach.length() == 0) {
                            return;
                        } else {
                            Toast.makeText(getApplication(), "Posting...", Toast.LENGTH_SHORT).show();
                            writeNewPost(userId, user.username, title, howToReach, experience, mLatitude, mLongitude);
                        }

                        setEditingEnabled(true);
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        setEditingEnabled(true);
                    }
                });
    }

    private void setEditingEnabled(boolean enabled) {
        mTitle.setEnabled(enabled);
        mHowToReach.setEnabled(enabled);
        mExperience.setEnabled(enabled);
        if (enabled) {
            mSubmitButton.setVisibility(View.VISIBLE);
        } else {
            mSubmitButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocation != null)
            mLocation.cancelTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void writeNewPost(String userId, String username, String title, String howToReach, String experience, double latitude, double longitude) {

        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, username, title, howToReach, experience, latitude, longitude);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    public static String getGoogleMapThumbnailUrl(double lati, double longi) {
        String URL = "http://maps.google.com/maps/api/staticmap?center=" + lati + "," + longi +
                "&zoom=14&size=400x400&sensor=false&markers=color:red%7C" + lati + "," + longi;
        return URL;
    }
}