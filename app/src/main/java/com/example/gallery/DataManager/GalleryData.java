package com.example.gallery.DataManager;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.options.StorageDownloadFileOptions;
import com.example.gallery.MainActivity;
import com.example.gallery.S3.DataCallback;
import com.example.gallery.S3.DataPreferences;
import com.example.gallery.S3.DataReciever;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GalleryData {

    private ArrayList<String> keys;
    private ArrayList<String> urls;

    private DataPreferences pref;
    private Boolean cached;

    // Use the methods in DataReciver to get all the data
    // and send it to individual data

    public GalleryData(DataPreferences preferences) {

        pref = preferences;

        keys = new ArrayList<String>();
        urls = new ArrayList<String>();

        cached = pref.arePreferencesCached(DataPreferences.ARE_URLS_CACHED) &&
                pref.arePreferencesCached(DataPreferences.ARE_KEYS_CACHED);

        DataReciever.init();

        if(!cached){
            loadKeys();
        } else {
            loadPref();
        }
    }

    private void sendData() {
        new VideosData(keys, urls);
        new PicturesData(keys, urls);
        new MusicData(keys, urls);
    }

    public ArrayList<String> getKeys() {
        return keys;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    private void loadKeys() {
        DataReciever.INSTANCE.getKeys(new DataCallback() {
            @Override
            public void onSuccess(ArrayList<String> list) {
                keys.clear();
                keys.addAll(list);
                Log.i("GalleryData : loadKeys()", String.valueOf(keys));
                loadUrls();
            }
            @Override
            public void onError() {
                    Log.i("Error", "GalleryData : loadKeys()");
            }
        });
    }

    private void loadUrls() {
        DataReciever.INSTANCE.getUrls(new DataCallback() {
            @Override
            public void onSuccess(ArrayList<String> list) {
                urls.clear();
                urls.addAll(list);

                // We notify adapter to use Data.urls to get thumbnails
                Log.i("GalleryData : loadUrls()", String.valueOf(list));
                DataReciever.initialize(list.size());

                // Save and Send data to all fragments
                save();
                sendData();
            }
            @Override
            public void onError() {
                    Log.i("Error", "GalleryData : loadUrls()");
            }
        });
    }

    public static void downloadFinish(Context context) {
    }

    private void loadPref() {
        ArrayList<String> list1 = pref.getSavedPreferences(DataPreferences.SAVE_URLS);
        urls.clear();
        urls.addAll(list1);

        ArrayList<String> list2 = pref.getSavedPreferences(DataPreferences.SAVE_KEYS);
        keys.clear();
        keys.addAll(list2);

        DataReciever.initialize(urls.size());

        Log.i("GalleryData", "loadPref: " + keys + urls);

        // Sending data to all fragments
        sendData();

        Log.i("Loading Cached :", "Data.urls & Keys");
    }

    private void save() {
        // save preferences
        pref.savePreferences(keys, DataPreferences.SAVE_KEYS);
        pref.savePreferences(urls, DataPreferences.SAVE_URLS);
    }

}
