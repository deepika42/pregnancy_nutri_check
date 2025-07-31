package com.example.pregnancynutricheck;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("Registered")
public class login extends AppCompatActivity {
    Button btnLogin,btnRegister;
    EditText phone,password,name;
    ProgressBar loading;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        phone=findViewById(R.id.phone);
        loading=findViewById(R.id.loading);
        password=findViewById(R.id.password);
        sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLoggin()){
            Intent intent = new Intent(getApplicationContext(),TabActivity.class);
            startActivity(intent);
        }

        //LOGIN BUTTON LOGIC
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mPhone=phone.getText().toString().trim();
                String mPassword=password.getText().toString().trim();

                if (!mPhone.isEmpty() || !mPassword.isEmpty()) {
                    Login(mPhone,mPassword);
                    //shared pref
                    phone.setText("");
                    password.setText("");
                    Toast.makeText(login.this,"Saved",Toast.LENGTH_SHORT).show();
                }
                else{
                    phone.setError("Enter phone");
                    password.setError("Enter password");
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Signup_Form.class);
                startActivity(intent);
            }
        });

    }

    private void Login(final String phone, final String password) {

        getIp ip = new getIp();
        String del = ip.getIp();
        RequestQueue requestQueue = Volley.newRequestQueue(login.this);
        //String URL = "http://192.168.0.103:8080/login";
        String URL = ""+del+":8080/login";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", phone);
            jsonObject.put("password", password);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        final String requestBody = jsonObject.toString();
        Log.d("str","str is"+requestBody);

        ConnectionManager.sendData(requestBody, requestQueue, URL, new ConnectionManager.VolleyCallback(){
            @Override
            public void onSuccessResponse(String result) {

                //System.out.print("Bool" + result);
                Log.d("RESULT","RESULTS "+result);
                if (result.equals("1")) {
                    loading.setVisibility(View.VISIBLE);
                    Toast.makeText(login.this, "Hello "+phone, Toast.LENGTH_SHORT).show();

                    sessionManager.createSession(phone);
                    System.out.println("Phone"+phone);
                    Intent intent = new Intent(getApplicationContext(), TabActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(login.this, "Try Again.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(login.this,
                        "Volley needs attention" + error,
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }}