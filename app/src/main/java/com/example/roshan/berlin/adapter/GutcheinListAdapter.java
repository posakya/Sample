package com.example.roshan.berlin.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.roshan.berlin.R;
import com.example.roshan.berlin.dbHandler.DBHandler;
import com.example.roshan.berlin.home.Menu;
import com.example.roshan.berlin.model.Gutschein_Model;

import java.util.ArrayList;

/**
 * Created by roshan on 11/27/17.
 */

public class GutcheinListAdapter extends ArrayAdapter {

    private ArrayList<Gutschein_Model> arrayList=new ArrayList<>();
    private Context context;
    private DBHandler db;
    private int resource;
    private LayoutInflater inflater;

    public GutcheinListAdapter( Context context,  int resource, ArrayList<Gutschein_Model> arrayList) {
        super(context, resource,arrayList);
        this.arrayList = arrayList;
        this.context = context;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;
        db=new DBHandler(context);
        if (convertView == null) {
            holder=new ViewHolder();
            convertView = inflater.inflate(R.layout.gutchein, null);
            holder.txt_price =  convertView.findViewById(R.id.voucher_price);
            holder.imageView = convertView.findViewById(R.id.voucher_image);
            holder.btn_price = convertView.findViewById(R.id.btn_add_to_cart);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Gutschein_Model gutschein_model=arrayList.get(position);
        System.out.println("Price3 :"+gutschein_model.getTextPrice());
        holder.txt_price.setText("€"+gutschein_model.getTextPrice());
        Glide.with(context).load(gutschein_model.getVoucher_image()).thumbnail(0.3f).into(holder.imageView);

        String discount=gutschein_model.getDiscountPrice();
        final String price=gutschein_model.getTextPrice();
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

        return convertView;
    }


    private class ViewHolder{
        private TextView txt_price;
        private ImageView imageView;
        private Button btn_price;
    }
}
