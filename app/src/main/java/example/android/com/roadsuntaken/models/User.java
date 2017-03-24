package example.android.com.roadsuntaken.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Shivam Garg on 24-03-2017.
 */

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

}
