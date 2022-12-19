package com.example.oldiary;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ReplyListAdapter extends ArrayAdapter<ReplyData> {
    private int mResource;
    private List<ReplyData> mItems;
    private LayoutInflater mInflater;

    public ReplyListAdapter(Context context, int resorce, List<ReplyData> items) {
        super(context, resorce, items);
        mResource = resorce;
        mItems = items;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView != null) {
            view = convertView;
        }
        else {
            view = mInflater.inflate(mResource, null);
        }

        ReplyData item = mItems.get(position);
        ImageView iconImg = view.findViewById(R.id.iconImage);
        iconImg.setImageResource(item.getIconId());
        TextView name = view.findViewById(R.id.name);
        TextView text = view.findViewById(R.id.textReply);
        name.setText(item.getUserName());
        text.setText(item.getText());

        return view;
    }

}
