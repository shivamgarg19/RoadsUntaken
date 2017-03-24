package example.android.com.roadsuntaken;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import example.android.com.roadsuntaken.fragment.MyPostsFragment;
import example.android.com.roadsuntaken.fragment.TopPostsFragment;
import example.android.com.roadsuntaken.fragment.RecentPostsFragment;

/**
 * Created by Shivam Garg on 24-03-2017.
 */

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences firstLaunch = getPreferences(Context.MODE_PRIVATE);
        if (firstLaunch.getBoolean("firstLaunch", true)) {
            firstLaunch.edit().putBoolean("firstLaunch", false).apply();
            startActivity(new Intent(MainActivity.this, HelpActivity.class));
        }

        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[]{
                    new RecentPostsFragment(),
                    new TopPostsFragment(),
                    new MyPostsFragment(),
            };
            private final String[] mFragmentNames = new String[]{
                    getString(R.string.heading_recent),
                    getString(R.string.heading_top_posts),
                    getString(R.string.heading_my_posts)
            };

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_new_post);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewPlaceActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.about) {
            showAboutDialogBox();
            return true;
        } else if (i == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showAboutDialogBox() {
        AlertDialog.Builder aboutDialogBox = new AlertDialog.Builder(MainActivity.this);
        aboutDialogBox.setTitle("About");
        aboutDialogBox.setMessage(getResources().getString(R.string.about));
        aboutDialogBox.setPositiveButton("Tell a friend", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String stringToShare = "Roads Untaken app:- https://play.google.com/store/apps/details?id=example.android.com.roadsuntaken";
                sharingIntent.putExtra(Intent.EXTRA_TEXT, stringToShare);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        aboutDialogBox.show();
    }

}
