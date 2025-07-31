package com.example.pregnancynutricheck;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class HomeFragment extends Fragment {

    String phone = "";
    TextView text;
    SessionManager sessionManager;
    TextView mWeight,mHeight,mAge,mWeek;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sessionManager = new SessionManager(getActivity());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        phone = user.get(sessionManager.PHONE);

        text = view.findViewById(R.id.name);
        text.setText(phone);

        mWeight=view.findViewById(R.id.weight);
        mHeight=view.findViewById(R.id.height);
        mAge=view.findViewById(R.id.age);
        mWeek=view.findViewById(R.id.week);



        getInfo();

        // Inflate the layout for this fragment
//        setHasOptionsMenu(true);
        return view;

    }
    public void getInfo(){
        getIp ip = new getIp();
        String del = ip.getIp();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String URL = ""+del+":8080/getinfo";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", phone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String requestBody = jsonObject.toString();
        Log.d("str", "str is" + requestBody);

        ConnectionManager.sendData(requestBody, requestQueue, URL, new ConnectionManager.VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                Log.d("result is ", "" + result);
                if (result != null) {
                    try {
                        if (sessionManager.isLoggin()) {
                            JSONArray jsonArray = new JSONArray(result);
                            Log.d("jsonAray", "" + jsonArray);
                            Log.d("Size of JSON Array", "" + jsonArray.length());
                            int i;
                            for (i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                String weight = null, height = null, age = null,week = null;
                                try {
                                    weight = jsonObject1.getString("weight");
                                    height = jsonObject1.getString("height");
                                    age = jsonObject1.getString("age");
                                    week = jsonObject1.getString("week");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                mWeight.setText(weight);
                                mHeight.setText(height);
                                mAge.setText(age);
                                mWeek.setText(week);

                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Error found", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error: ", "Volley needs attention");
            }

        });
    }

}