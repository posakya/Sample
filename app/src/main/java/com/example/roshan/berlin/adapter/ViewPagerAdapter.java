package com.example.roshan.berlin.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.roshan.berlin.R;
import com.example.roshan.berlin.model.GalleryModel;

import java.util.ArrayList;


/**
 * Created by Ashu on 10/10/2017.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<GalleryModel> galleries=new ArrayList<>();


    public ViewPagerAdapter(Context context,ArrayList<GalleryModel> galleries) {
        this.context = context;
       layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.galleries=galleries;
    }

    @Override
    public int getCount() {
       // return images.length;
        System.out.println("Size :"+galleries.size());
        return galleries.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (LinearLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

       // layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view= layoutInflater.inflate(R.layout.image_view,null);
        ImageView imageView= view.findViewById(R.id.imageView);
        GalleryModel gallery=galleries.get(position);
        System.out.println("value :"+gallery.getId());
        Glide.with(context).load(gallery.getImage()).placeholder(R.drawable.ic_gallery).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

//        ViewPager vp= (ViewPager) container;
//        View view=(View) object;
//        vp.removeView(view);
        container.removeView((LinearLayout) object);

    }
}