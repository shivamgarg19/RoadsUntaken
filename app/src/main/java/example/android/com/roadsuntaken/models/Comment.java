package example.android.com.roadsuntaken.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Shivam Garg on 24-03-2017.
 */

@IgnoreExtraProperties
public class Comment {

    public String uid;
    public String author;
    public String text;

    public Comment() {
    }

    public Comment(String uid, String author, String text) {
        this.uid = uid;
        this.author = author;
        this.text = text;
    }

}
