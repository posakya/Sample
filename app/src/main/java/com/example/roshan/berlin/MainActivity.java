package com.example.roshan.berlin;

import android.os.AsyncTask;
import android.os.Handler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.roshan.berlin.HttpHandler.HttpHandler;
import com.example.roshan.berlin.LoginAndRegister.LoginActivity;
import com.example.roshan.berlin.SubCategory.SubCategory;
import com.example.roshan.berlin.adapter.SlidingMenuAdapter;
import com.example.roshan.berlin.dbHandler.DBHandler;
import com.example.roshan.berlin.navMenu.Home;
import com.example.roshan.berlin.navMenu.History;
import com.example.roshan.berlin.navMenu.Location;
import com.example.roshan.berlin.model.ItemSlideMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    boolean doubleBackToExitPressedOnce = false;
    private List<ItemSlideMenu> listSliding;
    RelativeLayout mainView,drawerView;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    DBHandler db;
    private ImageView userImage;
    TextView txt_username;
    String username;
    String image;
    SharedPreferences sp;
    String username1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Init component
        userImage=(ImageView)findViewById(R.id.user_image);
        txt_username=(TextView)findViewById(R.id.userName);
        listViewSliding = (ListView) findViewById(R.id.lv_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (RelativeLayout) findViewById(R.id.Drawer_View);
        mainView = (RelativeLayout) findViewById(R.id.mainView);
        listSliding = new ArrayList<>();
        db=new DBHandler(getApplicationContext());

        try{
            sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            username1=sp.getString("username",null);
            String password1=sp.getString("password",null);

            if (username1!=null && password1!=null){
               new UserProfile().execute();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }


        //Add item for sliding list
        listSliding.add(new ItemSlideMenu(R.drawable.ic_home, "Home"));
        listSliding.add(new ItemSlideMenu(R.drawable.history, "History"));
        listSliding.add(new ItemSlideMenu(R.drawable.placeholder, "Find Location"));
        listSliding.add(new ItemSlideMenu(R.drawable.tag, "Offers"));
        listSliding.add(new ItemSlideMenu(R.drawable.support, "Support"));
        listSliding.add(new ItemSlideMenu(R.drawable.notification, "Notification"));
        listSliding.add(new ItemSlideMenu(R.drawable.logout, "Log Out"));
        adapter = new SlidingMenuAdapter(this, listSliding);
        listViewSliding.setAdapter(adapter);

        //Display icon to open/ close sliding list
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);


        //Set title
        setTitle(listSliding.get(0).getTitle());
        //item selected
        listViewSliding.setItemChecked(0, true);
        //Close menu
        drawerLayout.closeDrawer(drawerView);

        //remove shadow from drawer

        drawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));

        //Display fragment 1 when start
        replaceFragment(0);
        //Hanlde on item click

        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Set title
                setTitle(listSliding.get(position).getTitle());
                //item selected
                listViewSliding.setItemChecked(position, true);
                //Replace fragment
                replaceFragment(position);
                //Close menu
                drawerLayout.closeDrawer(drawerView);
            }
        });

         toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_opened, R.string.drawer_closed){
             @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mainView.setTranslationX(slideOffset * drawerView.getWidth());
                drawerLayout.bringChildToFront(drawerView);
                drawerLayout.requestLayout();
            }

        };

        drawerLayout.setDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    //Create method replace fragment

    private void replaceFragment(int pos) {
       // Fragment fragment = null;
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        switch (pos) {
            case 0:
                fragmentManager.beginTransaction().replace(R.id.main_content,new Home()).commit();
                break;
            case 1:
                fragmentManager.beginTransaction().replace(R.id.main_content,new History()).addToBackStack(null).commit();
                break;
            case 2:
                fragmentManager.beginTransaction().replace(R.id.main_content,new SubCategory()).addToBackStack(null).commit();
                break;
            case 6:
                db.delete();
                SharedPreferences settings =getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                settings.edit().clear().apply();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
            default:
               // startService(new Intent(getApplicationContext(), ChatHeadService.class));
                fragmentManager.beginTransaction().replace(R.id.main_content,new Home()).commit();
                break;

        }

//        if(null!=fragment) {
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.replace(R.id.main_content, fragment);
//            transaction.addToBackStack(null);
//            transaction.commit();
//        }
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this,"Touch again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                    db.deleteVoucher();
                    finishAffinity();
                }
            }, 2000);
        } else {
            super.onBackPressed();
            return;
        }
    }
    class UserProfile extends AsyncTask<String,Void,String > {



        private String url1= Constant_Url.Base_Url+"user_profile.php?Email="+username1;

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler sh=new HttpHandler();
            String jsonStr=sh.makeServiceCall(url1);
            System.out.println("URL :"+jsonStr);
            System.out.println("API :"+url1);

            return jsonStr;
        }

        @Override
        protected void onPostExecute(String jsonStr) {
            super.onPostExecute(jsonStr);

            if (jsonStr !=null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String image1 = Constant_Url.Base_Url+"/Image/"+jsonObject.optString("Image");
                        String phone1=jsonObject.optString("Contact");
                        String email1=jsonObject.optString("Email");
                        String username1 = jsonObject.optString("Username");

                        Glide.with(getApplicationContext()).load(image1).into(userImage);
                        txt_username.setText(username1);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

}