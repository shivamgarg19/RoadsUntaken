package example.android.com.roadsuntaken.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shivam Garg on 24-03-2017.
 */

public class Post {

    public String uid;
    public String author;
    public String title;
    public String howToReach;
    public String experience;
    public double latitude;
    public double longitude;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Post() {
    }

    public Post(String uid, String author, String title, String body, String experience, double latitude, double longitude) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.howToReach = body;
        this.experience = experience;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("howToReach", howToReach);
        result.put("experience", experience);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }

}
