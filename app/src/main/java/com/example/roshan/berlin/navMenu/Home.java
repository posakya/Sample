package com.example.roshan.berlin.navMenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roshan.berlin.MainActivity;
import com.example.roshan.berlin.R;
import com.example.roshan.berlin.home.Gallery;
import com.example.roshan.berlin.home.Home_inside;
import com.example.roshan.berlin.home.Info;
import com.example.roshan.berlin.home.Menu;

public class Home extends Fragment {
    View view;
    private TabLayout tabLayout;
    public Home() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home, container, false);
        tabLayout = view.findViewById(R.id.tabs);

            setHasOptionsMenu(true);

            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.getTabAt(0).setCustomView(R.layout.table_layout);
            tabLayout.getTabAt(1).setCustomView(R.layout.table_layout);
            tabLayout.getTabAt(2).setCustomView(R.layout.table_layout);
            tabLayout.getTabAt(3).setCustomView(R.layout.table_layout);
            View tab1_view = tabLayout.getTabAt(0).getCustomView();
            View tab2_view = tabLayout.getTabAt(1).getCustomView();
            View tab3_view = tabLayout.getTabAt(2).getCustomView();
            View tab4_view = tabLayout.getTabAt(3).getCustomView();
            TextView tab1_title =  tab1_view.findViewById(R.id.tab_title);
            ImageView img1 = tab1_view.findViewById(R.id.tab_img);
            TextView tab2_title = tab2_view.findViewById(R.id.tab_title);
            ImageView img2 = tab2_view.findViewById(R.id.tab_img);
            TextView tab3_title = tab3_view.findViewById(R.id.tab_title);
            ImageView img3 = tab3_view.findViewById(R.id.tab_img);
            TextView tab4_title = tab4_view.findViewById(R.id.tab_title);
            ImageView img4 = tab4_view.findViewById(R.id.tab_img);
            tab1_title.setText("Home");
            img1.setImageResource(R.drawable.ic_home);
            tab2_title.setText("Menü");
            img2.setImageResource(R.drawable.ic_menu);
            tab3_title.setText("Galerie");
            img3.setImageResource(R.drawable.ic_gallery);
            tab4_title.setText("Info");
            img4.setImageResource(R.drawable.ic_info);
        getFragmentManager().beginTransaction().replace(R.id.content,new Home_inside()).commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        getActivity().setTitle("Home");
                        getFragmentManager().beginTransaction().replace(R.id.content,new Home_inside()).commit();
                    break;

                    case 1:
                        getActivity().setTitle("Menü");
                        getFragmentManager().beginTransaction().replace(R.id.content,new Menu()).addToBackStack(null).commit();
                        break;

                    case 2:
                        getActivity().setTitle("Galerie");
                        getFragmentManager().beginTransaction().replace(R.id.content,new Gallery()).addToBackStack(null).commit();
                        break;

                    case 3:
                        getActivity().setTitle("Info");
                    getFragmentManager().beginTransaction().replace(R.id.content,new Info()).addToBackStack(null).commit();
                    break;

                    default:
                        startActivity(new Intent(getActivity(),MainActivity.class));
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        getActivity().setTitle("Home");
                        getFragmentManager().beginTransaction().replace(R.id.content,new Home_inside()).commit();
                        break;

                    case 1:
                        getActivity().setTitle("Menü");
                        getFragmentManager().beginTransaction().replace(R.id.content,new Menu()).addToBackStack(null).commit();
                        break;

                    case 2:
                        getActivity().setTitle("Galerie");
                        getFragmentManager().beginTransaction().replace(R.id.content,new Gallery()).addToBackStack(null).commit();
                        break;

                    case 3:
                        getActivity().setTitle("Info");
                        getFragmentManager().beginTransaction().replace(R.id.content,new Info()).addToBackStack(null).commit();
                        break;

                    default:
                        startActivity(new Intent(getActivity(),MainActivity.class));
                        break;
                }

            }
        });
        return view;
    }
}
