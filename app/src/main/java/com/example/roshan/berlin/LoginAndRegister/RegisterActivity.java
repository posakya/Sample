package com.example.roshan.berlin.LoginAndRegister;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roshan.berlin.Constant_Url;
import com.example.roshan.berlin.HttpHandler.HttpHandler;
import com.example.roshan.berlin.MainActivity;
import com.example.roshan.berlin.R;
import com.example.roshan.berlin.dbHandler.DBHandler;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A login screen that offers login via email/password.
 */

public class RegisterActivity extends AppCompatActivity implements OnClickListener {
    ImageView imageView;
    DBHandler db;
    ProgressDialog progressDialog;
    private static int RESULT_LOAD_IMAGE = 1;
    String url= Constant_Url.Base_Url+"register.php";
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    String email,password,confirm_pass,phone,username;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView,edit_username,edit_confirm_pass,edit_phone;
    private Button btn_signUp;
    private TextView txt_signIn;
    final int RQS_IMAGE1 = 1;
    Uri source1;
    Bitmap bm1;
    private String filePath = null;
    File file;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        imageView = (ImageView) findViewById(R.id.circular_image);

        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RQS_IMAGE1);
            }
        });
        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Set up the login form.
        db=new DBHandler(getApplicationContext());
        try {
            Cursor cr=db.getUserProfile();
            int count=cr.getCount();
            System.out.println("Size :"+cr.getCount());
            if (count>0){
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        edit_confirm_pass = (EditText) findViewById(R.id.confirm_password);
        edit_phone = (EditText) findViewById(R.id.phone);
        edit_username = (EditText) findViewById(R.id.username);
        btn_signUp = (Button) findViewById(R.id.up);
        txt_signIn = (TextView) findViewById(R.id.txt_sign_in);
        btn_signUp.setOnClickListener(this);
        txt_signIn.setOnClickListener(this);
        mPasswordView = (EditText) findViewById(R.id.password);

        progressDialog=new ProgressDialog(RegisterActivity.this);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }


    @Override
    public void onClick(View view) {
        if (view==btn_signUp){
            register();
            Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
            animation1.setDuration(1000);
            view.startAnimation(animation1);
        }

        if (view==txt_signIn){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }
    }

    public void register() {
        email = mEmailView.getText().toString();
        username = edit_username.getText().toString();
        password = mPasswordView.getText().toString();
        phone = edit_phone.getText().toString();
        confirm_pass = edit_confirm_pass.getText().toString();
        if (email.equals("")) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
        } else if (username.equals("")) {
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
        } else if (phone.equals("")) {
            Toast.makeText(this, "Enter Phone number", Toast.LENGTH_SHORT).show();
        } else if (password.equals("")) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        } else if (confirm_pass.equals("")) {
            Toast.makeText(this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
        } else if (!Objects.equals(password, confirm_pass)) {
            Toast.makeText(this, "Password and Confirm Password doesn't match", Toast.LENGTH_SHORT).show();
        } else if (file == null) {
            Toast.makeText(this, "Insert image", Toast.LENGTH_SHORT).show();
        } else {
            UploadTask uploadTask = new UploadTask(username, email, password, confirm_pass, file, phone);
            uploadTask.execute();
    }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case RQS_IMAGE1:
                    source1 = data.getData();
                    try {
                        System.out.println("Bitmap path = "+source1.getPath());
                        bm1 = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(source1));
                        imageView.setImageBitmap(bm1);

                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
                        Cursor cursor = getContentResolver().query(source1, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String filePath = cursor.getString(columnIndex);
                        cursor.close();

                        file = new File(filePath);

                        System.out.println("Image :"+bm1);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    }

    private class UploadTask extends AsyncTask<String ,Integer, String>{

        String username;
        String email;
        String password;
        String confirm_pass;
        File image;
        String contact;

        public UploadTask(String username, String email, String password, String confirm_pass, File image, String contact) {
            this.username = username;
            this.email = email;
            this.password = password;
            this.confirm_pass = confirm_pass;
            this.image = image;
            this.contact = contact;
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
                    .addFormDataPart("Username",username)
                    .addFormDataPart("Email",email)
                    .addFormDataPart("Password",password)
                    .addFormDataPart("Confirm_pass",confirm_pass)
                    .addFormDataPart("Image", image.getName(),
                            RequestBody.create(MediaType.parse("image/png"), image))
                    .addFormDataPart("Contact",phone)
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
            progressDialog.setMessage("");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            if (progressDialog.isShowing())
                progressDialog.dismiss();

            try {
                JSONObject jsonObjet = new JSONObject(result);
                String code = jsonObjet.getString("success");

                if (code.equals("1")) {
                    SharedPreferences sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("username",email);
                    editor.putString("password", password);
                    editor.apply();
                    UserProfile userProfile=new UserProfile();
                    userProfile.execute();
                    Toast.makeText(RegisterActivity.this, "" + code, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }
                if (code.equals("0")) {
                    Toast.makeText(RegisterActivity.this, "" + code, Toast.LENGTH_SHORT).show();
                }
            } catch (
                    JSONException e
                    )

            {
                e.printStackTrace();
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
            Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_LONG).show();
        }
    }
    private class UserProfile extends AsyncTask<String,Void,String > {

        private DBHandler db=new DBHandler(getApplicationContext());


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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

