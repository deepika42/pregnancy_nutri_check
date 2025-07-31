package com.example.pregnancynutricheck;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class DietFragment extends Fragment {

    String phone = "";
    SessionManager sessionManager;
    TextView mFood1,mFood2,mFood3,mWeek;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_diet, container, false);
        sessionManager = new SessionManager(getActivity());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        phone = user.get(sessionManager.PHONE);

        mWeek=view.findViewById(R.id.week);
        mFood1=view.findViewById(R.id.food1);
        mFood2=view.findViewById(R.id.food2);
        mFood3=view.findViewById(R.id.food3);

        getInfo();

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
//    public void getDiet(){
//        getIp ip = new getIp();
//        String del = ip.getIp();
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        String URL = ""+del+":8080/getdiet";
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("phone", phone);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        final String requestBody = jsonObject.toString();
//        Log.d("str", "str is" + requestBody);
//
//        ConnectionManager.sendData(requestBody, requestQueue, URL, new ConnectionManager.VolleyCallback() {
//            @Override
//            public void onSuccessResponse(String result) {
//                Log.d("result is ", "" + result);
//                if (result != null) {
//                    try {
//                        if (sessionManager.isLoggin()) {
//                            JSONArray jsonArray = new JSONArray(result);
//                            Log.d("jsonAray", "" + jsonArray);
//                            Log.d("Size of JSON Array", "" + jsonArray.length());
//                            int i;
//                            for (i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//
//                                String week = null, food1 = null, food2 = null, food3 = null;
//                                try {
//                                    week = jsonObject1.getString("week");
//                                    food1 = jsonObject1.getString("food1");
//                                    food2 = jsonObject1.getString("food2");
//                                    food3 = jsonObject1.getString("food3");
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                mWeek.setText(week);
//                                mFood1.setText(food1);
//                                mFood2.setText(food2);
//                                mFood3.setText(food3);
//
//                            }
//                        }
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else{
//                    Toast.makeText(getActivity(), "Error found", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("error: ", "Volley needs attention");
//            }
//
//        });
//    }
}
