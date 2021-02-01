package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{
    private ArrayList<RecyclerItem> mRecyclerList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onCallClick(int posititon);
        void onVideoCallClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView,mImageView2;
        public TextView mText;
        public ImageView mCallImg,mVideoCallImg;

        public RecyclerViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.profileImg);
            mImageView2 = itemView.findViewById(R.id.ratingImg);
            mText = itemView.findViewById(R.id.nameText);
            mCallImg = itemView.findViewById(R.id.callImg);
            mVideoCallImg = itemView.findViewById(R.id.VideoCallImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            mCallImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onCallClick(position);
                        }
                    }
                }
            });

            mVideoCallImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onVideoCallClick(position);
                        }
                    }
                }
            });
        }
    }

    public RecyclerAdapter(ArrayList<RecyclerItem> recyclerList){
        mRecyclerList = recyclerList;
    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        RecyclerViewHolder rvh = new RecyclerViewHolder(v, mListener);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        RecyclerItem currentItem = mRecyclerList.get(position);

        holder.mImageView.setImageResource(currentItem.getProfImg());
        holder.mImageView2.setImageResource(currentItem.getRatingImg());
        holder.mText.setText(currentItem.getNameSurname());
    }

    @Override
    public int getItemCount() {
        return mRecyclerList.size();
    }
}
