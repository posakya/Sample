package com.example.roshan.berlin.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.roshan.berlin.dbHandler.DBHandler;
import com.example.roshan.berlin.R;


/**
 * Created by roshan on 11/7/17.
 */

public class HomeAdapter extends SimpleCursorAdapter {
    Context context;
    private DBHandler db;
    public HomeAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.menu_item, parent, false);
        ViewHolder holder = new ViewHolder();
        convertView.setTag(holder);
        holder.txt_desc =  convertView.findViewById(R.id.home_desc);
        holder.txt_title = convertView.findViewById(R.id.home_title);
        holder.imageView = convertView.findViewById(R.id.home_image);
        db=new DBHandler(context);
        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        ViewHolder holder = (ViewHolder) view.getTag();
        String title = cursor.getString(cursor.getColumnIndex(db.item_name));
        String description = cursor.getString(cursor.getColumnIndex(db.item_description));
        String image = cursor.getString(cursor.getColumnIndex(db.image));

        holder.txt_title.setText(title);
        holder.txt_desc.setText(Html.fromHtml(description));
        Glide.with(context).load(image).thumbnail(0.3f).into(holder.imageView);
        System.out.println("Cursor_Image :"+image);
    }
    private class ViewHolder{
        private TextView txt_title,txt_desc;
        private ImageView imageView;
    }
}
