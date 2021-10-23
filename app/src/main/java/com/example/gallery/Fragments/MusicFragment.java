package com.example.gallery.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.options.StorageDownloadFileOptions;
import com.example.gallery.Adapter.MainAdapter;
import com.example.gallery.Adapter.MusicAdapter;
import com.example.gallery.DataManager.MusicData;
import com.example.gallery.R;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicFragment extends Fragment implements MusicAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private static MusicAdapter adapter;
    private static ArrayList<String> urls;
    private static ArrayList<String> keys;
    private static Boolean created;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MusicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MusicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MusicFragment newInstance(String param1, String param2) {
        MusicFragment fragment = new MusicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_music, container, false);

        check();
        // Add the following lines to create RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewPictures);
        adapter = new MusicAdapter(getContext(), keys);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);

        created = true;
        Log.i("MusicFragment", "onCreateView: fragment");
        return view;
    }

    public static void notifyChange(ArrayList<String> keyList, ArrayList<String> urlList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                check();
                keys.clear();
                keys.addAll(keyList);
                urls.clear();
                urls.addAll(urlList);
                if(created!=null && created) adapter.notifyDataSetChanged();
            }
        });
    }

    private static void check(){
        if(keys == null){
            keys = new ArrayList<String>();
        }
        if(urls == null){
            urls = new ArrayList<String>();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        check(position);
    }

    private void check(int position) {
        if(isAlreadyDownloaded(position)){
            Toast t = Toast.makeText(getContext(),
                    "You already downloaded this video",
                    Toast.LENGTH_LONG);
            t.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
            t.show();
        }
        else{
            downloadAlert(position, keys.get(position));
        }
    }

    public static void downloadFinish(Context context) {
        Toast t = Toast.makeText(context,
                "Video downloaded!",
                Toast.LENGTH_LONG);
        t.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
        t.show();
    }

    private boolean isAlreadyDownloaded(int position) {
        return MusicData.isDownloaded.get(position);
    }

    private void downloadAlert(int position, String key) {
        new AlertDialog.Builder(getContext())
                .setMessage("Do you want to download this video?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    MusicData.downloadFile(position, key);
                    dialog.dismiss();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

}