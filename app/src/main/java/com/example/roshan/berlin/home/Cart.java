package com.example.roshan.berlin.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.roshan.berlin.R;
import com.example.roshan.berlin.adapter.Cursor_Adapter;
import com.example.roshan.berlin.dbHandler.DBHandler;
import com.example.roshan.berlin.payment.Payement;

import java.util.Timer;
import java.util.TimerTask;

public class Cart extends AppCompatActivity {
    DBHandler db;
    Cursor c;
    Float total;
    float sub_total;
    private ListView listView;
    Toolbar toolbar;
    TextView txt_total;
    EditText edit_item_name,edit_quantity_value,edit_price;
    Button btn_update,btn_delete,btn_conrifm;
    String itemName,itemQuantity,itemPrice;
    cursor_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_list);
        db=new DBHandler(getApplicationContext());
        listView=(ListView)findViewById(R.id.listView);
        btn_conrifm = (Button)findViewById(R.id.payment);
        txt_total=(TextView)findViewById(R.id.total_value);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        c=db.getGutschein();
        String[] from={db.item,db.price,db.quantity};
        int[] to={R.id.txt_artikel,R.id.txt_preis,R.id.txt_menge};
        adapter=new cursor_adapter(Cart.this,R.layout.fragment_cart,c,from,to,0);
        listView.setAdapter(adapter);
        c.requery();
        adapter.notifyDataSetChanged();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // This code will always run on the UI thread, therefore is safe to modify UI elements.
                        Cursor c = db.total();
                        sub_total = c.getFloat(c.getColumnIndex("total_price"));
                        txt_total.setText(String.format("€ %.2f", sub_total));
                    }
                });

            }
        },0, 1000);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = null;
                cursor = (Cursor) adapterView.getItemAtPosition(i);
                itemName=cursor.getString(cursor.getColumnIndex(db.item));
                itemQuantity=cursor.getString(cursor.getColumnIndex(db.quantity));
                itemPrice=cursor.getString(cursor.getColumnIndex(db.price));
                dialog_method();
            }
        });

        btn_conrifm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Cart.this, Payement.class);
                intent.putExtra("total",sub_total);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void dialog_method(){
        final Dialog dialog = new Dialog(Cart.this);
        dialog.setContentView(R.layout.cart_dialog);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setTitle("Cart");
        dialog.setCanceledOnTouchOutside(true);
        edit_item_name=dialog.findViewById(R.id.edit_item_name);
        edit_quantity_value=dialog.findViewById(R.id.edit_quantity);
        edit_price=dialog.findViewById(R.id.edit_price);
        btn_delete=dialog.findViewById(R.id.btn_delete);
        btn_update=dialog.findViewById(R.id.btn_update);
        edit_item_name.setText(itemName);
        edit_quantity_value.setText(itemQuantity);
        final String price=itemPrice.replaceAll("[€ ]","");
        edit_price.setText(price);



        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                view.startAnimation(animation1);
                db.delete_gutchein(itemName);
                dialog.dismiss();
                c.requery();
                adapter.notifyDataSetChanged();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                view.startAnimation(animation1);
                Float subtotal=Float.parseFloat(itemQuantity);
                total=(Float.parseFloat(edit_price.getText().toString())/subtotal)*Float.parseFloat(edit_quantity_value.getText().toString());
                System.out.println("Total :"+total);
                String preis=String .valueOf(total);
                System.out.println("Preis :"+edit_price.getText().toString());
                db.insertGutschein(itemName,edit_quantity_value.getText().toString(), preis);
                c.requery();
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    class cursor_adapter extends SimpleCursorAdapter{
        Context context;
        private DBHandler db;
        public cursor_adapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View convertView = inflater.inflate(R.layout.fragment_cart, parent, false);
           ViewHolder holder = new ViewHolder();
            convertView.setTag(holder);
            holder.txt_item =  convertView.findViewById(R.id.txt_artikel);
            holder.txt_price = convertView.findViewById(R.id.txt_preis);
            holder.txt_quantity = convertView.findViewById(R.id.txt_menge);

            db=new DBHandler(context);
            return convertView;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            super.bindView(view, context, cursor);
            ViewHolder holder = (ViewHolder) view.getTag();
            String item_name = cursor.getString(cursor.getColumnIndex(db.item));
            String item_price = cursor.getString(cursor.getColumnIndex(db.price));
            String quantity = cursor.getString(cursor.getColumnIndex(db.quantity));
            holder.txt_item.setText(item_name);
            String price= "€ "+item_price;
            holder.txt_price.setText(price);
            holder.txt_quantity.setText(quantity);
        }

        class ViewHolder{
            TextView txt_price,txt_item,txt_quantity;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
