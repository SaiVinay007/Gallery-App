package com.example.gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Gallery;

import com.example.gallery.DataManager.GalleryData;
import com.example.gallery.Fragments.DownloadsFragment;
import com.example.gallery.Fragments.MusicFragment;
import com.example.gallery.Fragments.PicturesFragment;
import com.example.gallery.Fragments.VideosFragment;
import com.example.gallery.S3.DataPreferences;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    final Fragment videosFragment = new VideosFragment();
    final Fragment musicFragment = new MusicFragment();
    final Fragment picturesFragment = new PicturesFragment();
    final Fragment downloadsFragment = new DownloadsFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = videosFragment;

    private DataPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = new DataPreferences(this);

        Log.i("Main activity", "onCreate: Main activity");

        fm.beginTransaction().add(R.id.fragmentContainer, downloadsFragment, "4").hide(downloadsFragment).commit();
        fm.beginTransaction().add(R.id.fragmentContainer, picturesFragment, "3").hide(picturesFragment).commit();
        fm.beginTransaction().add(R.id.fragmentContainer, musicFragment, "2").hide(musicFragment).commit();
        fm.beginTransaction().add(R.id.fragmentContainer, videosFragment, "1").commit();
        Log.i("Main activity", "onCreate: after commits");

        // Nav bar
        BottomNavigationView bottomNavigation = (BottomNavigationView)
                findViewById(R.id.bottomNavigation);

        bottomNavigation.setOnItemSelectedListener(
                new BottomNavigationView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.nav_videos:
                                fm.beginTransaction().hide(active).show(videosFragment).commit();
                                active = videosFragment;
                                return true;
                            case R.id.nav_music:
                                fm.beginTransaction().hide(active).show(musicFragment).commit();
                                active = musicFragment;
                                return true;
                            case R.id.nav_pictures:
                                fm.beginTransaction().hide(active).show(picturesFragment).commit();
                                active = picturesFragment;
                                return true;
                            case R.id.nav_downloads:
                                fm.beginTransaction().hide(active).show(downloadsFragment).commit();
                                active = downloadsFragment;
                                return true;
                        }
                        return false;
                    }
                });

        // Initialize the data reciever and populate data
        getData(pref);
    }

    // Get data and send to all fragments
    private void getData(DataPreferences pref) {
        // call gallery data to get data from s3 and
        // send to other data classes
        GalleryData galleryData = new GalleryData(pref);
        Log.i("Main: getData()", "onCreate: ");
    }
}