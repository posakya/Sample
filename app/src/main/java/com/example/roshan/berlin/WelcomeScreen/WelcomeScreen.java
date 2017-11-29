package com.example.roshan.berlin.WelcomeScreen;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.roshan.berlin.Constant_Url;
import com.example.roshan.berlin.LoginAndRegister.LoginActivity;
import com.example.roshan.berlin.LoginAndRegister.RegisterActivity;
import com.example.roshan.berlin.dbHandler.DBHandler;
import com.example.roshan.berlin.HttpHandler.HttpHandler;
import com.example.roshan.berlin.MainActivity;
import com.example.roshan.berlin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class WelcomeScreen extends AppCompatActivity {
    ImageView imageView;
    DBHandler db;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 0;
    List<String> urls = new ArrayList<>();
    String url = Constant_Url.Base_Url+"api.php";
    String url1 = Constant_Url.Base_Url+"home_api.php";
    ProgressDialog progressDialog;
    String menu_type,item_name,image,desc,date,item_price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setDrawingCacheEnabled(true);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
        imageView.setAnimation(animation);


        animation.setAnimationListener(new Animation.AnimationListener() {
                                           @Override
                                           public void onAnimationStart(Animation animation) {
                                              // progressDialog=new ProgressDialog(getApplicationContext());
                                               db=new DBHandler(getApplicationContext());
//                                               urls.add("https://jsonplaceholder.typicode.com/posts/1");
//                                               Json myAsyncTask = new Json(WelcomeScreen.this, urls);
//                                               myAsyncTask.execute();
                                               new Json_Home().execute();
                                               new Json().execute();

                                           }

                                           @Override
                                           public void onAnimationEnd(Animation animation) {
                                               finish();
                                               Cursor cr=db.getHomeData();
                                               startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                               System.out.println("Cursor size "+cr.getCount());
                                                }

                                           @Override
                                           public void onAnimationRepeat(Animation animation) {

                                           }
                                       }
        );


        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_STORAGE);
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_STORAGE);

                // MY_PERMISSIONS_REQUEST_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private class Json extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            if (progressDialog==null) {
//                progressDialog=new ProgressDialog(WelcomeScreen.this);
//                progressDialog.setMessage("Loading");
//                progressDialog.setCancelable(false);
//            }
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler sh=new HttpHandler();
            String jsonStr=sh.makeServiceCall(url);
            System.out.println("URL :"+jsonStr);

            if (jsonStr !=null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        menu_type = jsonObject.optString("Menu_Type");
                        item_name = jsonObject.optString("Item_Name");
                        item_price = jsonObject.optString("Item_Price");
                        image = Constant_Url.Base_Url+"Image/"+jsonObject.optString("Image");
                        desc = jsonObject.optString("Item_Desc");
                        date=jsonObject.optString("Update_Date");
                        System.out.println("menu_type :" + menu_type);
                        System.out.println("Image :"+image);
                        System.out.println("Inserting :");
                        db.insertdata(menu_type,item_name,desc,item_price,image,date);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return jsonStr;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            if (progressDialog!=null && progressDialog.isShowing())
//                progressDialog.dismiss();
        }
        }

    private class Json_Home extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler sh=new HttpHandler();
            String jsonStr=sh.makeServiceCall(url1);
            System.out.println("URL1 :"+jsonStr);

            if (jsonStr !=null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String title = jsonObject.optString("Title");
                        String image = Constant_Url.Base_Url+"Image/"+jsonObject.optString("Image");
                        String desc = jsonObject.optString("Description");
                        System.out.println("Image :"+image);
                        System.out.println("Inserting :");
                        db.insertHome(title,desc,image);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return jsonStr;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog.setMessage("Loading");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
