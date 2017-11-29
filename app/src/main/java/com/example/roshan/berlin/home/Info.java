package com.example.roshan.berlin.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.roshan.berlin.R;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

/**
 * A simple {@link Fragment} subclass.
 */
public class Info extends Fragment implements ObservableScrollViewCallbacks {

private View view;
    ImageView imageView,imageLogo;
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
        observableScrollView.setScrollViewCallbacks(this);
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
