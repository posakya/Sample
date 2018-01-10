package com.example.roshan.berlin.home;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.roshan.berlin.R;
import com.example.roshan.berlin.SubCategory.SubCategory;
//import com.example.roshan.berlin.adapter.ListAdapter;
import com.example.roshan.berlin.adapter.Cursor_Adapter;
import com.example.roshan.berlin.dbHandler.DBHandler;
import com.example.roshan.berlin.model.MenuType;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */

public class Menu extends Fragment {

    ArrayList<MenuType> typeArrayList=new ArrayList<>();
    View view;
    ListView listvew;

    SharedPreferences prefs;
    String desc="Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s";
    public Menu() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.list_view, container, false);
        listvew=view.findViewById(R.id.listView);

        if(typeArrayList!=null) {
            typeArrayList.clear();
        }

        typeArrayList.add(new MenuType("Vorspeisen",R.drawable.chicken_vindaloo,desc));
        typeArrayList.add(new MenuType("Suppen",R.drawable.chicken_vindaloo,desc));
        typeArrayList.add(new MenuType("Beilagen",R.drawable.chicken_vindaloo,desc));
        typeArrayList.add(new MenuType("Salate",R.drawable.chicken_vindaloo,desc));
        typeArrayList.add(new MenuType("Vegetarisch",R.drawable.chicken_vindaloo,desc));
        typeArrayList.add(new MenuType("Hühnerfleisch",R.drawable.chicken_vindaloo,desc));
        typeArrayList.add(new MenuType("Lammfleisch",R.drawable.chicken_vindaloo,desc));
        typeArrayList.add(new MenuType("Ente",R.drawable.chicken_vindaloo,desc));
        typeArrayList.add(new MenuType("Fisch/Scampi",R.drawable.chicken_vindaloo,desc));
        typeArrayList.add(new MenuType("Reisgerichte",R.drawable.chicken_vindaloo,desc));
        typeArrayList.add(new MenuType("Tandoori Grill",R.drawable.chicken_vindaloo,desc));
        typeArrayList.add(new MenuType("Desserts",R.drawable.chicken_vindaloo,desc));
        typeArrayList.add(new MenuType("Cocktails",R.drawable.chicken_vindaloo,desc));



        ListAdapter adapter = new ListAdapter(getActivity(),R.layout.fragment_menu, typeArrayList);



        listvew.setAdapter(adapter);

        listvew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MenuType menuType=typeArrayList.get(i);
                prefs = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("title", menuType.getTitle());
                editor.apply();

                getFragmentManager().beginTransaction().replace(R.id.content,new SubCategory()).addToBackStack(null).commit();
             //   Toast.makeText(getActivity(), "You have clicked "+menuType.getTitle() , Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private class ListAdapter extends ArrayAdapter {

        Context context;
        ArrayList<MenuType> menuTypeArrayList=new ArrayList<>();
        LayoutInflater inflater;
        int resource;

        ListAdapter(Context context, int resource, ArrayList<MenuType> menuTypeArrayList) {
            super(context, resource,menuTypeArrayList);
            this.resource = resource;
            this.menuTypeArrayList = menuTypeArrayList;
            this.context=context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {

          ViewHolder holder;
            if (convertView == null) {
                holder=new ViewHolder();
                convertView = inflater.inflate(R.layout.fragment_menu, null);
                holder.txt_desc =  convertView.findViewById(R.id.item_desc);
                holder.txt_title = convertView.findViewById(R.id.item_title);
                holder.imageView = convertView.findViewById(R.id.item_image);
                holder.btn_view = convertView.findViewById(R.id.btn_view_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final MenuType menuType=menuTypeArrayList.get(position);
            holder.txt_title.setText(menuType.getTitle());
            System.out.println("Title :"+menuType.getTitle());
            holder.txt_desc.setText(menuType.getDesc());
            Glide.with(context).load(menuType.getImage()).asBitmap().thumbnail(0.5f).into(holder.imageView);
            holder.btn_view.setClickable(false);
            holder.btn_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    prefs = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("title", menuType.getTitle());
                    editor.apply();
                    getFragmentManager().beginTransaction().replace(R.id.content,new SubCategory()).addToBackStack(null).commit();

                }
            });

            return convertView;
        }

        private class ViewHolder{
            private TextView txt_title,txt_desc;
            private ImageView imageView;
            private Button btn_view;
        }
    }


}
