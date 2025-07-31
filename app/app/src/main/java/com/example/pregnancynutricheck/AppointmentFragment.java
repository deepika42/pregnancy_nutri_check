package com.example.pregnancynutricheck;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends Fragment implements
        View.OnClickListener {

    EditText medtName, medtDetail, medtNote;
    Button btnDatePicker, btnTimePicker, mbtnAdd, mbtnCancel;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    public static SQLiteHelper mSQLiteHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_appointment, container, false);

        ImageButton imageButton = (ImageButton) view.findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CalendarActivity.class);
                startActivity(intent);
            }
            });
        medtName=view.findViewById(R.id.edtName);
        medtDetail=view.findViewById(R.id.edtDetail);
        medtNote=view.findViewById(R.id.edtNote);

        mbtnAdd=(Button)view.findViewById(R.id.save);
        mbtnCancel=(Button)view.findViewById(R.id.cancel);
        btnDatePicker=(Button)view.findViewById(R.id.btn_date);
        btnTimePicker=(Button)view.findViewById(R.id.btn_time);
        txtDate=(EditText)view.findViewById(R.id.edtDate);
        txtTime=(EditText)view.findViewById(R.id.edtTime);

        mSQLiteHelper = new SQLiteHelper(getContext(),"RECORDDB.sqlite",null,1);

        //creating table
        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS RECORD(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, detail VARCHAR, date VARCHAR, time VARCHAR, note VARCHAR)");


        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        mbtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mSQLiteHelper.insertData(
                            medtName.getText().toString().trim(),
                            medtDetail.getText().toString().trim(),
                            txtDate.getText().toString().trim(),
                            txtTime.getText().toString().trim(),
                            medtNote.getText().toString().trim()
                    );
                    Toast.makeText(getActivity(),"Added successfully",Toast.LENGTH_SHORT).show();
                    medtName.setText("");
                    medtDetail.setText("");
                    txtDate.setText("");
                    txtTime.setText("");
                    medtNote.setText("");

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        mbtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AddCard.class));
            }
        });




        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }


}