package com.example.roshan.berlin.navMenu;/**
 * Created by NgocTri on 10/18/2015.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roshan.berlin.R;


public class History extends Fragment {

    TextView payment;
    public History() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment2, container, false);
        payment=rootView.findViewById(R.id.payment);
        SharedPreferences prefs = getActivity().getSharedPreferences("payment", Context.MODE_PRIVATE);
        String payment1 = prefs.getString("payment", null);
        payment.setText("Sie besitzen den Gutschein von "+payment1);
        return rootView;
    }
}
