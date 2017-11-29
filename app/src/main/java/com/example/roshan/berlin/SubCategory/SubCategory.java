package com.example.roshan.berlin.SubCategory;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.roshan.berlin.MainActivity;
import com.example.roshan.berlin.dbHandler.DBHandler;
import com.example.roshan.berlin.R;
import com.example.roshan.berlin.adapter.Cursor_Adapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubCategory extends Fragment {
    View view;
    DBHandler myDb;
    GridView gridView;
    Cursor_Adapter cursorAdapter;
    String title;
    SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.grid_view, container, false);
        myDb=new DBHandler(getActivity());
        gridView=view.findViewById(R.id.gridView);
        setHasOptionsMenu(true);

        try {
            prefs = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
            title = prefs.getString("title", null);
            System.out.println("Text :" +title);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        Cursor cursor = myDb.getData(title);
      //  Toast.makeText(getActivity(), "Total item"+cursor.getCount(), Toast.LENGTH_SHORT).show();
        getActivity().setTitle(title);
        String[] from = {DBHandler.item_name, DBHandler.item_description, DBHandler.item_Price, DBHandler.image};
        int[] to = {R.id.item_title, R.id.item_desc,R.id.btn_price,R.id.item_image };
        cursorAdapter=new Cursor_Adapter(getActivity(),R.layout.item_list,cursor,from,to,0);
        gridView.setAdapter(cursorAdapter);
        return view;
    }
}
