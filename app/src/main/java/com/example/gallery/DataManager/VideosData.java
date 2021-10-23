package com.example.gallery.DataManager;

import android.util.Log;

import com.example.gallery.Fragments.VideosFragment;

import java.util.ArrayList;

public class VideosData {

    private ArrayList<String> keys;
    private ArrayList<String> urls;

    public VideosData(ArrayList<String> keyList, ArrayList<String> urlList) {
        keys = new ArrayList<String>();
        urls = new ArrayList<String>();
        makeData(keyList, urlList);
    }

    private void makeData(ArrayList<String> keyList, ArrayList<String> urlList) {
        int i=0;
        for(String s:keyList) {
            if(s.startsWith("videos")){
                keys.add(s);
                urls.add(urlList.get(i));
            }
            i++;
        }
        Log.i("VideosData", "makeData: "+urls);
        VideosFragment.notifyChange(urls);
    }

}
