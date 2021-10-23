package com.example.gallery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.gallery.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MusicAdapter  extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private final ArrayList<String> mKeys;
    private LayoutInflater mInflater;
    private MusicAdapter.ItemClickListener mClickListener;

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public MusicAdapter(Context context, ArrayList<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mKeys = data;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_music, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mKeys.get(id);
    }

    @Override
    public int getItemCount() {
        return mKeys.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener((View.OnClickListener) this);
        }

        void bindTo(String key) {
            String text = key.substring(key.indexOf("/") + 1, key.indexOf("."));
            myTextView.setText(text);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    public void setClickListener(MusicAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}
