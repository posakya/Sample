package com.example.roshan.berlin.home;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roshan.berlin.R;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class Info extends Fragment implements ObservableScrollViewCallbacks {

private View view;
    ImageView imageView,imageLogo;
    ImageButton btn_youtube,btn_facebook,btn_google;
    TextView txt_Status;
    public Info() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_info, container, false);
        ObservableScrollView observableScrollView = (ObservableScrollView)view.findViewById(R.id.observable_scrollview);
        imageView=view.findViewById(R.id.image_logo);
        imageLogo=view.findViewById(R.id.image_logo1);
        txt_Status=view.findViewById(R.id.status);
        btn_facebook=view.findViewById(R.id.facebook);
        btn_google=view.findViewById(R.id.google);
        btn_youtube=view.findViewById(R.id.youtube);
        observableScrollView.setScrollViewCallbacks(this);

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("EE");
        SimpleDateFormat sdf_time=new SimpleDateFormat("HH");
        String time=sdf_time.format(date);
        String dateString = sdf.format(date);
        int time1=Integer.parseInt(time);
        System.out.println("Today :"+time);


        if (dateString.equals("Sun") || dateString.equals("Mon") || dateString.equals("Tue") ||dateString.equals("Wed")||dateString.equals("Thu")){
            if (time1>=12 && time1<=24){
                txt_Status.setText("OPEN");
            }else {
                txt_Status.setText("CLOSE");
            }
        }else  if (dateString.equals("Fri") || dateString.equals("Sat"))
            if (time1>=12 && time1<=2){
                txt_Status.setText("OPEN");
            }else {
                txt_Status.setText("CLOSE");
            }

        btn_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(R.string.youtube))));
            }
        });

        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(String .valueOf(R.string.google))));
            }
        });

        btn_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/vedis.restaurant")));
            }
        });

        return view;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        imageView.setTranslationY(scrollY / 2);
        imageLogo.setTranslationY(scrollY/4);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
