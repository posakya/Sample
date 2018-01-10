package com.example.roshan.berlin.home;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.roshan.berlin.Constant_Url;
import com.example.roshan.berlin.HttpHandler.HttpHandler;
import com.example.roshan.berlin.MainActivity;
import com.example.roshan.berlin.R;
import com.example.roshan.berlin.adapter.HomeAdapter;
import com.example.roshan.berlin.dbHandler.DBHandler;
import com.example.roshan.berlin.model.Category;
import com.example.roshan.berlin.model.Gutschein_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home_inside extends Fragment {
    ProgressDialog progressDialog;
    private ArrayList<Category> categories = new ArrayList<>();
    View view;
    Dialog dialog;
    DBHandler db;
    private SwipeRefreshLayout swipeRefreshLayout;
    String url1= Constant_Url.Base_Url+"gutchein_api.php";
    private GridView gridView;
    EditText edit_time,edit_date,edit_username,edit_email,edit_message,edit_contact;
    Button btn_submit;
    private HomeAdapter pAdapter;
    DBHandler myDb;
    GridView gridview;
    Calendar mcurrent= Calendar.getInstance();
    String username,email,message,contact,quantity;
    Spinner spinner;
    Dialog dialog1;
    ArrayList<Gutschein_Model> gutschein_models=new ArrayList<>();

    String  url = Constant_Url.Base_Url+"reservation.php";
    String notify_url=Constant_Url.Base_Url+"send_notification.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.grid_view, container, false);
        gridView = view.findViewById(R.id.gridView);
        myDb=new DBHandler(getActivity());
        Cursor cursor = myDb.getHomeData();
      //  Toast.makeText(getActivity(), "Total item"+cursor.getCount(), Toast.LENGTH_SHORT).show();

        progressDialog=new ProgressDialog(getActivity());

        String[] from = {DBHandler.item_name, DBHandler.item_description, DBHandler.image};
        int[] to = {R.id.home_title, R.id.home_desc,R.id.item_image };
        pAdapter=new HomeAdapter(getActivity(),R.layout.menu_item,cursor,from,to,0);
        gridView.setAdapter(pAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               switch (i){

                   //reservation
                   case 0:
                           dialog = new Dialog(getActivity(),android.R.style.Theme_Holo_Light);

                       //dialog action
                       dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                       dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                       dialog.setContentView(R.layout.reservation);
                       dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
                      //   dialog.setTitle("Reservation");
                         dialog.setCanceledOnTouchOutside(true);

                       Toolbar toolbar=dialog.findViewById(R.id.toolbar);
                       toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
                       toolbar.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              dialog.dismiss();
                          }
                      });

                        edit_date=dialog.findViewById(R.id.txt_date);
                        edit_time=dialog.findViewById(R.id.txt_time);
                        edit_username=dialog.findViewById(R.id.txt_username);
                        edit_email=dialog.findViewById(R.id.txt_email);
                        edit_message=dialog.findViewById(R.id.txt_message);
                        edit_contact=dialog.findViewById(R.id.txt_phone);
                        btn_submit=dialog.findViewById(R.id.btn_submit);
                        spinner=dialog.findViewById(R.id.quantity);

                       String[] quantity1 = {"No. of Person","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};
                       ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, quantity1);
                       adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                       spinner.setAdapter(adapter);
                       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                           @Override
                           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                               ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                               quantity=adapterView.getItemAtPosition(i).toString();
                           }

                           @Override
                           public void onNothingSelected(AdapterView<?> adapterView) {

                           }
                       });

                       edit_time.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               time();
                           }
                       });

                       edit_date.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               date();
                           }
                       });
                       btn_submit.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               detail();
                               Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                               animation1.setDuration(1000);
                               view.startAnimation(animation1);
                           }
                       });

                       dialog.show();
                       break;

                   //gutchein
                   case 1:
                        db=new DBHandler(getActivity());
                       dialog1 = new Dialog(getActivity(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                       dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                       dialog1.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                       dialog1.setContentView(R.layout.activity_gutchein);

                       dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
                       //   dialog.setTitle("Reservation");
                       dialog1.setCanceledOnTouchOutside(true);
                       //    dialog.getActionBar().setDisplayHomeAsUpEnabled(true);
                       swipeRefreshLayout= dialog1.findViewById(R.id.swipe_refresh);



                       Toolbar toolbar1=dialog1.findViewById(R.id.toolbar);
                       ImageView back=toolbar1.findViewById(R.id.backArrow);
                       ImageView cart_image=toolbar1.findViewById(R.id.cart);
                       cart_image.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               startActivity(new Intent(getActivity(),Cart.class));
                           }
                       });
                       final Button btn_count=toolbar1.findViewById(R.id.count);

                      // getActivity().invalidateOptionsMenu();
try{
    new Timer().schedule(new TimerTask() {
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // This code will always run on the UI thread, therefore is safe to modify UI elements.
                    Cursor cursor1=db.getGutschein();
                    int count=cursor1.getCount();
                    btn_count.setText(""+count);
                }
            });

        }
    },0, 1000);
}catch (NullPointerException e){
    e.printStackTrace();
}


                      // toolbar1.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
                       back.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               dialog1.dismiss();
                           }
                       });

                       if (gutschein_models!=null){
                           gutschein_models.clear();
                       }

                       new JsonTask().execute();
//                       Cursor cursor=myDb.getVoucherDate();
//                       String[] from = {DBHandler.item_Price,DBHandler.image};
//                       int[] to = {R.id.voucher_price, R.id.voucher_image};
//                       Gutschein_Adapter gutschein_adapter=new Gutschein_Adapter(getActivity(),R.layout.gutchein,cursor,from,to,0);
//                       gridView.setAdapter(gutschein_adapter);
                      // gutschein_adapter.notifyDataSetChanged();
                       swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);

                       swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                           @Override
                           public void onRefresh() {
                              new JsonTask().execute();
                               try {
                                   Thread.sleep(3000);
                                   swipeRefreshLayout.setRefreshing(false);
                               } catch (InterruptedException e) {
                                   e.printStackTrace();
                               }

                           }
                       });

                       dialog1.show();
                       break;
                   default:
                       startActivity(new Intent(getActivity(), MainActivity.class));
                       break;
               }
            }
        });

        return view;
    }

    public void time(){
        int hour = mcurrent.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrent.get(Calendar.MINUTE);
        final int second = mcurrent.get(Calendar.SECOND);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), R.style.MyMaterialTheme,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                edit_time.setText( selectedHour + ":" + selectedMinute + ":" + second);
            }
        }, hour, minute, true);
//        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    public void date(){

        final Calendar c = Calendar.getInstance();
        int mYear1 = c.get(Calendar.YEAR);
        int mMonth1 = c.get(Calendar.MONTH);
        int mDay1 = c.get(Calendar.DAY_OF_MONTH); // add here +1 if you don't
        //want user to select current date also
        c.set(Calendar.DAY_OF_MONTH, mDay1);

        int mYear = mcurrent.get(Calendar.YEAR);
        int mMonth = mcurrent.get(Calendar.MONTH);
        int mDay = mcurrent.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(getActivity(),R.style.MyMaterialTheme,new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                selectedmonth = selectedmonth + 1;
                edit_date.setText("" + selectedyear + "-" + selectedmonth + "-" + selectedday);
            }
        }, mYear, mMonth, mDay);
       // mDatePicker.setTitle("Select Date");
        mDatePicker.getDatePicker().setMinDate(c.getTimeInMillis());

        mDatePicker.show();

    }

    public void detail(){
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        String token=sharedpreferences.getString("token",null);

        username = edit_username.getText().toString();
        email = edit_email.getText().toString();
        message = edit_message.getText().toString();
        contact = edit_contact.getText().toString();
        if (username.equals("") || email.equals("")||contact.equals("")||edit_date.getText().toString().equals("")||edit_time.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Please fill all the form", Toast.LENGTH_SHORT).show();
        }else{
            UploadFileToServer uploadFileToServer=new UploadFileToServer(edit_date.getText().toString(),edit_time.getText().toString(),username,message,email,quantity,contact,"0",token);
            uploadFileToServer.execute();
        }


    }

    //data upload to server
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {

        String date;
        String time;
        String username;
        String message;
        String email;
        String quantity;
        String contact;
        String status;
        String token;

        UploadFileToServer(String date, String time, String username, String message, String email, String quantity, String contact, String status, String token) {
            this.date = date;
            this.time = time;
            this.username = username;
            this.message = message;
            this.email = email;
            this.quantity = quantity;
            this.contact = contact;
            this.status = status;
            this.token = token;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

//        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                return uploadFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() throws IOException {
           // String responseString = null;

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("Date",date)
                    .addFormDataPart("Time",time)
                    .addFormDataPart("Quantity",quantity)
                    .addFormDataPart("Username",username)
                    .addFormDataPart("Email",email)
                    .addFormDataPart("Contact",contact)
                    .addFormDataPart("Message",message)
                    .addFormDataPart("Status",status)
                    .addFormDataPart("Token",token)
                    .build();
            Request request = new Request.Builder().url(url).post(formBody).build();
            Response response = client.newCall(request).execute();
            String result=response.body().string();
            Log.e("message", result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObjet = new JSONObject(result);
                JSONObject data = jsonObjet.getJSONObject("notification");
                String code=data.getString("success");

                if (code.equals("1")) {
                    Toast.makeText(getActivity(), "" + code, Toast.LENGTH_SHORT).show();
                   // new PushNotification().execute();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                   // new GetContacts().execute();
                }
                if (code.equals("0")) {
                    Toast.makeText(getActivity(), "" + code, Toast.LENGTH_SHORT).show();
                }
            } catch (
                    JSONException e
                    )
            {
                e.printStackTrace();
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
            Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
        }
        }

//        private class PushNotification extends AsyncTask<String , Integer, String>{
//
//            @Override
//            protected String doInBackground(String... strings) {
//                try {
//                    return sendNotification();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @SuppressWarnings("deprecation")
//            private String sendNotification() throws IOException {
//                // String responseString = null;
//
//                OkHttpClient client = new OkHttpClient();
//                RequestBody formBody = new MultipartBody.Builder()
//                        .setType(MultipartBody.FORM)
//                        .addFormDataPart("Email",edit_email.getText().toString())
//                        .addFormDataPart("message","Hello its me roshan")
//                        .addFormDataPart("title","FCM notification")
//                        .build();
//                Request request = new Request.Builder().url(notify_url).post(formBody).build();
//                Response response = client.newCall(request).execute();
//                String result=response.body().string();
//                Log.e("message1", result);
//                return result;
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                super.onPostExecute(result);
//                try {
//                    JSONObject jsonObjet = new JSONObject(result);
//                    JSONObject data = jsonObjet.getJSONObject("notification");
//                    String code=data.getString("success");
//
//                    if (code.equals("1")) {
//                        Toast.makeText(getActivity(), "" + code, Toast.LENGTH_SHORT).show();
//                        // new GetContacts().execute();
//                    }
//                    if (code.equals("0")) {
//                        Toast.makeText(getActivity(), "" + code, Toast.LENGTH_SHORT).show();
//                    }
//                } catch (
//                        JSONException e
//                        )
//                {
//                    e.printStackTrace();
//                } catch (NullPointerException ex) {
//                    ex.printStackTrace();
//                }
//                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
//            }
//
//        }



    private class JsonTask extends AsyncTask<String,Void,String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler sh=new HttpHandler();
            String json=sh.makeServiceCall(url1);
            if (json !=null) {
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    System.out.println("Length :"+jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Gutschein_Model gutschein_model=new Gutschein_Model();

                        String price = jsonObject.optString("Price");
                        String Discount=jsonObject.optString("Discount");

                        String image = Constant_Url.Base_Url+"Image/"+jsonObject.optString("Image");
                        gutschein_model.setVoucher_image(image);
                        gutschein_model.setTextPrice(price);
                        gutschein_model.setDiscountPrice(Discount);

                        gutschein_models.add(gutschein_model);
                        System.out.println("Price :"+price);
                     //   myDb.insertVoucher(price,Discount,image);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                gridview=dialog1.findViewById(R.id.gridView);
//                if (gutschein_models!=null)
//                    gutschein_models.clear();
                GutcheinListAdapter adapter=new GutcheinListAdapter(getActivity(),R.layout.gutchein,gutschein_models);
                gridview.setAdapter(adapter);
            }
        }
    }

    private class GutcheinListAdapter extends ArrayAdapter {

        private ArrayList<Gutschein_Model> arrayList=new ArrayList<>();
        private Context context;
        private int resource;

        public GutcheinListAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<Gutschein_Model> arrayList) {
            super(context, resource, arrayList);
            this.arrayList = arrayList;
            this.context = context;
            this.resource = resource;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        private DBHandler db;

        private LayoutInflater inflater;

//        GutcheinListAdapter(Context context, int resource, ArrayList<Gutschein_Model> arrayList) {
//            super(context, resource,arrayList);
//            this.arrayList = arrayList;
//            this.context = context;
//            this.resource = resource;
//            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            ViewHolder holder;
            db=new DBHandler(context);
            if (convertView == null) {
                holder=new ViewHolder();
                convertView = inflater.inflate(R.layout.gutchein, null);
                holder.txt_price =  convertView.findViewById(R.id.voucher_price);
                holder.imageView = convertView.findViewById(R.id.voucher_image);
                holder.btn_price = convertView.findViewById(R.id.btn_add_to_cart);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Gutschein_Model gutschein_model=arrayList.get(position);
            System.out.println("Price3 :"+gutschein_model.getTextPrice());
            holder.txt_price.setText("€"+gutschein_model.getTextPrice());
            Glide.with(context).load(gutschein_model.getVoucher_image()).thumbnail(0.3f).into(holder.imageView);

            String discount=gutschein_model.getDiscountPrice();
            final String price=gutschein_model.getTextPrice();
            float discountAmount=Float.valueOf(discount)/Float.valueOf(price);
            final float total=Float.valueOf(price)-discountAmount*100;

            holder.btn_price.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                    animation1.setDuration(1000);
                    view.startAnimation(animation1);
                    String item="Gutschein €"+price;
                    String price=String .valueOf(total);
                    db.insertGutschein(item,"1",price);
                    Toast.makeText(context, "added to cart", Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }


        private class ViewHolder{
            private TextView txt_price;
            private ImageView imageView;
            private Button btn_price;
        }
    }

}
