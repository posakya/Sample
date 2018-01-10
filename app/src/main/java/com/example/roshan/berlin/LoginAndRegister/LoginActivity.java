package com.example.roshan.berlin.LoginAndRegister;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.example.roshan.berlin.Constant_Url;
import com.example.roshan.berlin.HttpHandler.HttpHandler;
import com.example.roshan.berlin.MainActivity;
import com.example.roshan.berlin.R;
import com.example.roshan.berlin.dbHandler.DBHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {


    // UI references.
    private EditText mPasswordView;
    private TextView tv_newhere, tv_forgotpw,tv_login;
    ImageView image;
    String url=Constant_Url.Base_Url+"login.php";
    String password,username;
    Button btn_sign_in;
    EditText editText_signin;
    TextView indian;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_sign_in=(Button)findViewById(R.id.sign_in_button);
        editText_signin=(EditText)findViewById(R.id.username);
        tv_newhere=(TextView)findViewById(R.id.tv_vedis3);
        indian=(TextView)findViewById(R.id.text_view_vedis);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/robot.ttf");
        indian.setTypeface(type);
        indian.setText(R.string.india);
        btn_sign_in.setOnClickListener(this);
        tv_newhere.setOnClickListener(this);
        tv_newhere.setText(Html.fromHtml(getString(R.string.signUp)));

       try{
           sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
           String username1=sp.getString("username",null);
           String password1=sp.getString("password",null);

           if (username1!=null && password1!=null){
               startActivity(new Intent(getApplicationContext(),MainActivity.class));
           }
       }catch (NullPointerException e){
           e.printStackTrace();
       }



        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {

                    return true;
                }
                return false;
            }
        });


    }

    @Override
    public void onClick(View view) {

        if (view == btn_sign_in) {
            Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
            animation1.setDuration(1000);
            view.startAnimation(animation1);
            login();
        }
        if (view == tv_newhere) {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        }
    }

    public void login(){
        username=editText_signin.getText().toString();
        password=mPasswordView.getText().toString();
        if (username.equals("")){
            Toast.makeText(this, "Enter the username", Toast.LENGTH_SHORT).show();
        }else if (password.equals("")){
            Toast.makeText(this, "Enter the password", Toast.LENGTH_SHORT).show();
        }else {
            UploadTask uploadTask=new UploadTask(username,password);
            uploadTask.execute();
        }

    }

    class UploadTask extends AsyncTask<String ,Integer, String>{

        String username;
        String password;

    public UploadTask(String username, String password ) {
        this.username = username;
        this.password = password;

    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            return uploadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    private String uploadFile() throws IOException {
        String responseString = null;

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("Email",username)
                .addFormDataPart("Password",password)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Response response = client.newCall(request).execute();
        String result=response.body().string();
        System.out.println("Result :"+result);
        return result;

    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }


    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);

        try {
            JSONObject jsonObjet = new JSONObject(result);
            JSONArray jsonArray=jsonObjet.getJSONArray("server_response");
            JSONObject jsonObject1=jsonArray.getJSONObject(0);
            String code = jsonObject1.getString("code");
            String message=jsonObject1.getString("message");

            if (code.equals("login_true")) {
                Toast.makeText(LoginActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                 sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                  editor.putString("username",username);
                editor.putString("password", password);
                editor.apply();
                UserProfile userProfile=new UserProfile();
                userProfile.execute();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
            if (code.equals("login_false")) {
                Toast.makeText(LoginActivity.this, "" + message, Toast.LENGTH_SHORT).show();
            }
        } catch (
                JSONException e
                )

        {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
      //  Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
    }
}


    class UserProfile extends AsyncTask<String,Void,String > {

        private Context context;
        private String email;
        private DBHandler db=new DBHandler(context);


        private String url1= Constant_Url.Base_Url+"user_profile.php?Email="+email;

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler sh=new HttpHandler();
            String jsonStr=sh.makeServiceCall(url1);
            System.out.println("URL :"+jsonStr);
            System.out.println("API :"+url1);
            if (jsonStr !=null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String image1 = Constant_Url.Base_Url+"/Image/"+jsonObject.optString("Image");
                        String phone1=jsonObject.optString("Contact");
                        String email1=jsonObject.optString("Email");
                        String username1 = jsonObject.optString("Username");
                        System.out.println("Username :" + username1);
                        db.insertUserProfile(phone1,email1,username1,image1);
                        Cursor cursor=db.getUserProfile();
                        System.out.println("Cursor size :"+cursor.getCount());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return jsonStr;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}

