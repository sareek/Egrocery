package com.knight.egrocery;

import com.knight.egrocery.AppController;
import com.knight.egrocery.Details;

import java.util.List;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class CustomList extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Details> Items;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomList(Activity activity, List<Details> Items) {
        this.activity = activity;
        this.Items = Items;
    }



    @Override
    public int getCount() {
        return Items.size();
    }

    @Override
    public Object getItem(int location) {
        return Items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_item, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView pid=(TextView)convertView.findViewById(R.id.pid);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView price = (TextView) convertView.findViewById(R.id.price);



        // getting movie data for the row
        Details d = Items.get(position);

        // thumbnail image
        thumbNail.setImageUrl(d.getThumbnailUrl(), imageLoader);

        // title
        pid.setText(String.valueOf(d.getId()));
        name.setText(d.getName());

        // rating
        price.setText(String.valueOf(d.getPrice()));


        return convertView;
    }


}