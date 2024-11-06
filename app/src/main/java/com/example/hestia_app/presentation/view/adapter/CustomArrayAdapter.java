package com.example.hestia_app.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.hestia_app.R;

import java.util.List;

public class  CustomArrayAdapter extends ArrayAdapter<String> {

    public CustomArrayAdapter(Context context, List<String> suggestions) {
        super(context, R.layout.item_dropdown, suggestions);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_dropdown, parent, false);
        }

        String suggestion = getItem(position);
        TextView textViewItem = convertView.findViewById(R.id.textViewItem);
        textViewItem.setText(suggestion);

        return convertView;
    }
}
