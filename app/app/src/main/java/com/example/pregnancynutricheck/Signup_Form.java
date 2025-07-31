package com.example.pregnancynutricheck;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Signup_Form extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener
{
    private Button register;
    private EditText name,phone,password,street,city,pincode;
    private Spinner city_spinner;
    String city_name;
    //6
    ProgressBar loading;
    final String URL ="http://192.168.43.61:8080/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__form);

        city_spinner = (Spinner) findViewById(R.id.city_spinner);

        name=findViewById(R.id.name);
        phone=findViewById(R.id.phone);
        password=findViewById(R.id.password);
        street=findViewById(R.id.street);
        pincode=findViewById(R.id.pincode);

        loading=findViewById(R.id.loading);
        register=findViewById(R.id.register);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.city,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city_spinner.setAdapter(adapter);
        city_spinner.setOnItemSelectedListener(this);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mName=name.getText().toString().trim();
                String mPhone=phone.getText().toString().trim();
                String mPassword=password.getText().toString().trim();

                String mStreet=street.getText().toString().trim();
               // String mCity=city.getText().toString().trim();
                String mPincode=pincode.getText().toString().trim();




                if (!mName.isEmpty() || !mPhone.isEmpty() || !mPassword.isEmpty() || !mStreet.isEmpty() || !mPincode.isEmpty()) {
                    if(mPhone.length()==10 && mPincode.length()==6) {
                        Register(mName, mPhone,mPassword, mStreet, city_name, mPincode);
                    }
                }
                else{
                    name.setError("Enter name");
                    phone.setError("Enter phone");
                    password.setError("Enter password");
                    street.setError("Enter street");
                    city.setError("Enter city");
                    pincode.setError("Enter pincode");
                }
            }
        });


    }
    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(getApplicationContext(), login.class);
        startActivity(intent);
        finish();
        return true;
    }

    private void Register(String mName, String mPhone, String mPassword, String mStreet ,String city_name, String mPincode) {
        getIp ip = new getIp();
        String del = ip.getIp();
        RequestQueue requestQueue = Volley.newRequestQueue(Signup_Form.this);
        String URL = ""+del+":8080/register";

        //String URL = "http://192.168.0.103:8080/register";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", mName);
            jsonObject.put("phone", mPhone);
            jsonObject.put("password", mPassword);
            jsonObject.put("street", mStreet);
            jsonObject.put("city",city_name);
            jsonObject.put("pincode", mPincode);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        final String requestBody = jsonObject.toString();
        Log.d("str_register","strREG is"+requestBody);

        ConnectionManager.sendData(requestBody, requestQueue, URL, new ConnectionManager.VolleyCallback(){
            @Override
            /*
            public void onSuccessResponse(String result) {
                Log.d("RESULT","RESULTS "+result);
                if (result.equals("1")) {
                    Toast.makeText(RegistrationActivity.this, result, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                //System.out.print("Bool" + result);
                 else {
                    Toast.makeText(RegistrationActivity.this, "Oops! Try Again.", Toast.LENGTH_SHORT).show();
                }
            }
            */


            public void onSuccessResponse(String result) {
                Log.d("RESULT","RESULTS "+result);
                if (result.equals("1")) {
                    Toast.makeText(Signup_Form.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), login.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Signup_Form.this, "Try again", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(Signup_Form.this,
                        "Volley needs attention" + error,
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        if(adapterView.getId()==R.id.city_spinner)
        {
//            ((TextView) adapterView.getChildAt(0)).setTextColor();

            city_name = adapterView.getItemAtPosition(i).toString();

//           Toast.makeText(this,city_name,Toast.LENGTH_SHORT).show();


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}