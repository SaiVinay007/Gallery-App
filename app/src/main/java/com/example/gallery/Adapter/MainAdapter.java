package com.example.gallery.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.gallery.R;

import java.util.ArrayList;
import java.util.HashMap;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private final ArrayList<String> mUrls;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public MainAdapter(Context context, ArrayList<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mUrls = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
//         holder.myImageView.setImageResource(R.drawable.ic_launcher_foreground);
        holder.bindTo(getItem(position));
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView myImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myImageView = itemView.findViewById(R.id.image);
            itemView.setOnClickListener((View.OnClickListener) this);
        }

        void bindTo(String url) {
            Glide.with(itemView)
                    .load(url)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(myImageView);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mUrls.get(id);
    }

    @Override
    public int getItemCount() {
        return mUrls.size();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}
