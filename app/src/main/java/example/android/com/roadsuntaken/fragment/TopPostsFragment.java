package example.android.com.roadsuntaken.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by Shivam Garg on 24-03-2017.
 */

public class TopPostsFragment extends PostListFragment {

    public TopPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        String myUserId = getUid();
        Query myTopPostsQuery = databaseReference.child("posts")
                .orderByChild("starCount");
        return myTopPostsQuery;
    }
}
