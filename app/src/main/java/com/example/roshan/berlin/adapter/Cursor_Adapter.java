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

public class Cursor_Adapter extends SimpleCursorAdapter {
    Context context;
    private DBHandler db;
    public Cursor_Adapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.item_list, parent, false);
        ViewHolder holder = new ViewHolder();
        convertView.setTag(holder);
        holder.txt_desc =  convertView.findViewById(R.id.item_desc);
        holder.txt_title = convertView.findViewById(R.id.item_title);
        holder.imageView = convertView.findViewById(R.id.item_image);
        holder.btn_price = convertView.findViewById(R.id.btn_price);
        db=new DBHandler(context);
        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        ViewHolder holder = (ViewHolder) view.getTag();
        String menu = cursor.getString(cursor.getColumnIndex(db.type_of_menu));
        String title = cursor.getString(cursor.getColumnIndex(db.item_name));
        String description = cursor.getString(cursor.getColumnIndex(db.item_description));
        String price = cursor.getString(cursor.getColumnIndex(db.item_Price));
        String image = cursor.getString(cursor.getColumnIndex(db.image));
        String date = cursor.getString(cursor.getColumnIndex(db.date));

        holder.txt_title.setText(title);
        System.out.println("Title :"+date);
        holder.txt_desc.setText(Html.fromHtml(description.replaceFirst(" ","")));
        holder.btn_price.setText("€ "+price);
        Glide.with(context).load(image).thumbnail(0.3f).into(holder.imageView);
        System.out.println("Cursor_Image :"+image);
    }
    private class ViewHolder{
        private TextView txt_title,txt_desc;
        private ImageView imageView;
        private Button btn_price;
    }
}
