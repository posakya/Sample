package com.example.roshan.berlin.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.roshan.berlin.R;
import com.example.roshan.berlin.dbHandler.DBHandler;
import com.example.roshan.berlin.model.Gutschein_Model;
import com.example.roshan.berlin.payment.Payement;

import java.util.ArrayList;
import java.util.jar.Pack200;

/**
 * Created by roshan on 11/15/17.
 */

public class Gutschein_Adapter extends SimpleCursorAdapter {
    Context context;
    private DBHandler db;
    public Gutschein_Adapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.gutchein, parent, false);
        ViewHolder holder = new ViewHolder();
        convertView.setTag(holder);
        holder.txt_price =  convertView.findViewById(R.id.voucher_price);
        holder.imageView = convertView.findViewById(R.id.voucher_image);
        holder.btn_price = convertView.findViewById(R.id.btn_add_to_cart);
        db=new DBHandler(context);
        return convertView;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        ViewHolder holder = (ViewHolder) view.getTag();

        final String price = cursor.getString(cursor.getColumnIndex(db.item_Price));
        String image = cursor.getString(cursor.getColumnIndex(db.image));
        final String discount = cursor.getString(cursor.getColumnIndex(db.discount));
        holder.txt_price.setText("€"+price);
        System.out.println("cost :"+price);

        float discountAmount=Float.valueOf(discount)/Float.valueOf(price);
        final float total=Float.valueOf(price)-discountAmount*100;

        holder.btn_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                view.startAnimation(animation1);
                String item="Gutschein"+price;
                db.insertGutschein(item,"1",String.valueOf(total));
                Toast.makeText(context, "added to cart", Toast.LENGTH_SHORT).show();

            }
        });


        Glide.with(context).load(image).thumbnail(0.3f).into(holder.imageView);
       // System.out.println("Cursor_Image :"+image);
    }
    private class ViewHolder{
        private TextView txt_price;
        private ImageView imageView;
        private Button btn_price;
    }
}
