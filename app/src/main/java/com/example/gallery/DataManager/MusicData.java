package com.example.gallery.DataManager;

import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.options.StorageDownloadFileOptions;
import com.example.gallery.Fragments.MusicFragment;
import com.example.gallery.Fragments.VideosFragment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MusicData {

    private ArrayList<String> keys;
    private ArrayList<String> urls;

    public static ArrayList<Boolean> isDownloaded;
    public static ArrayList<String> downloadedPaths;

    public MusicData(ArrayList<String> keyList, ArrayList<String> urlList) {
        keys = new ArrayList<String>();
        urls = new ArrayList<String>();
        isDownloaded = new ArrayList<>();
        downloadedPaths = new ArrayList<>();
        makeData(keyList, urlList);
    }

    private void makeData(ArrayList<String> keyList, ArrayList<String> urlList) {
        int i=0;
        for(String s:keyList) {
            if(s.startsWith("music")){
                keys.add(s);
                urls.add(urlList.get(i));
            }
            i++;
        }
        Log.i("MusicData", "makeData: "+keys);
        MusicFragment.notifyChange(keys, urls);
    }

    public static void downloadFile(int position, String key) {
        String[] parts = key.split("/");
        String path = Environment.
                getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                +"/"+ parts[parts.length-1];

        try {
            check_directory();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(path);
        if(file.exists()) {
            Log.i("Exists", "downloadFile: " + path);
        } else {
            try {
                file.createNewFile();
                Log.i("Created", "downloadFile: " + path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Amplify.Storage.downloadFile(
                key,
                new File(path),
                StorageDownloadFileOptions.defaultInstance(),
                progress -> Log.i("MyAmplifyApp", "Fraction completed: " + progress.getFractionCompleted()),
                result -> {
                    Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getName());
                    isDownloaded.set(position, true);
                    downloadedPaths.add(String.valueOf(result.getFile()));
                },
                error -> Log.e("MyAmplifyApp",  "Download Failure", error)
        );


    }

    private static void check_directory() throws IOException {
//        File directory = new File (Environment.
//                getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
//                "/Video_Gallery");
//        if (! directory.exists()){
//            directory.mkdirs();
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Files.createDirectories(Paths.get(String.valueOf(Environment.
                    getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))));
        }
    }
}
