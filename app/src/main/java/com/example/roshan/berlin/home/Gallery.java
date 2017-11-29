package com.example.roshan.berlin.home;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.roshan.berlin.Constant_Url;
import com.example.roshan.berlin.HttpHandler.HttpHandler;
import com.example.roshan.berlin.R;
import com.example.roshan.berlin.adapter.ViewPagerAdapter;
import com.example.roshan.berlin.model.GalleryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Gallery extends Fragment {
    private View view;
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;
    ProgressDialog progressDialog;
    ArrayList<GalleryModel> galleryModels=new ArrayList<>();
    String url= Constant_Url.Base_Url+"gallery_api.php";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_gallery, container, false);
        viewPager=view.findViewById(R.id.view_pager);
        progressDialog=new ProgressDialog(getActivity());
        new GalleryData().execute();
        return view;
    }

    class GalleryData extends AsyncTask<String, Void , String >{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("loading");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpHandler httpHandler=new HttpHandler();
            String jsonStr=httpHandler.makeServiceCall(url);
            System.out.println("URL :"+jsonStr);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String jsonStr) {
            super.onPostExecute(jsonStr);
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if (jsonStr!=null){
                try {
                    JSONArray jsonArray=new JSONArray(jsonStr);

                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        GalleryModel gallery=new GalleryModel();
                       gallery.setId(jsonObject.optString("Id"));
                       gallery.setImage(Constant_Url.Base_Url+"Image/"+jsonObject.optString("Image"));
                   //     System.out.println("Image2 :"+image);
                        galleryModels.add(gallery);
                    }

                    viewPagerAdapter=new ViewPagerAdapter(getActivity(),galleryModels);
                    viewPager.setAdapter(viewPagerAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
