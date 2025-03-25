package com.example.power_home;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class NotificationAdapter extends ArrayAdapter<NotificationItem> {

    private Activity activity;
    private List<NotificationItem> list;
    private int itemId;

    public NotificationAdapter(Activity activity, List<NotificationItem> list, int itemId) {
        super(activity, itemId, list);
        this.activity = activity;
        this.list = list;
        this.itemId = itemId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout = convertView;

        if (layout == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            layout = inflater.inflate(itemId, parent, false);
        }

        TextView title = layout.findViewById(R.id.notif_title);
        TextView desc = layout.findViewById(R.id.notif_desc);

        NotificationItem item = list.get(position);

        title.setText(item.getTitre());
        desc.setText(item.getDescription());

        // Change la couleur du titre selon le type de notif
        switch (item.getType()) {
            case 1: title.setTextColor(Color.parseColor("#4CAF50")); break; // Vert
            case 2: title.setTextColor(Color.parseColor("#F44336")); break; // Rouge
            default: title.setTextColor(Color.BLACK); break;
        }

        return layout;
    }
}
