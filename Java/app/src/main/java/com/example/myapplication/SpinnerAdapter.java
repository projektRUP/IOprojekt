package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter <SpinnerItem> {
    public SpinnerAdapter(Context context, ArrayList<SpinnerItem> spinnerItems){
        super(context, 0, spinnerItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position,  View convertView,  ViewGroup parent){
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_row, parent, false
            );
        }

        ImageView imageView = convertView.findViewById(R.id.image_view);

        SpinnerItem currentItem = getItem(position);

        if(currentItem != null) {
            imageView.setImageResource(currentItem.getSpinnerImg());
        }

        return convertView;
    }
}
