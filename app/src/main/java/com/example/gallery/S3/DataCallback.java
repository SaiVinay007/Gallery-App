package com.example.gallery.S3;

import java.util.ArrayList;

public interface DataCallback {
    void onSuccess(ArrayList<String> keyList);
    void onError();
}
