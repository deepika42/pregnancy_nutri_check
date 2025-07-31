package com.example.pregnancynutricheck;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class AddCard extends AppCompatActivity {

    ListView mListView;
    ArrayList<Model> mList;
    RecordAdapter mAdapter = null;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);


        mListView=findViewById(R.id.listview);
        mList= new ArrayList<>();
        mAdapter= new RecordAdapter(this,R.layout.row,mList);
        mListView.setAdapter(mAdapter);

        floatingActionButton=findViewById(R.id.floatAdd);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AppointmentFragment.class);
                startActivity(i);
            }
        });

        Cursor cursor = AppointmentFragment.mSQLiteHelper.getData("SELECT * FROM RECORD");
        mList.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String detail = cursor.getString(2);
            String date = cursor.getString(3);
            String time = cursor.getString(4);
            String note = cursor.getString(5);

            mList.add(new Model(id,name,detail,date,time,note));
        }
        mAdapter.notifyDataSetChanged();
        if(mList.size()==0){
            Toast.makeText(this, "No record found", Toast.LENGTH_SHORT).show();
        }

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                CharSequence[] items ={"Update","Delete"};

                AlertDialog.Builder dialog= new AlertDialog.Builder(AddCard.this);
                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if(i==0){
                            //update
                            Cursor c = AppointmentFragment.mSQLiteHelper.getData("SELECT id FROM RECORD");
                            ArrayList<Integer> arrID= new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }

                            showDialogUpdate(AddCard.this,arrID.get(position));
                        }
                        if(i==1){
                            //delete
                            Cursor c = AppointmentFragment.mSQLiteHelper.getData("SELECT id FROM RECORD");
                            ArrayList<Integer> arrID= new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

    }

    private void showDialogDelete(final int idRecord) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(AddCard.this);
        dialogDelete.setTitle("Warning");
        dialogDelete.setMessage("Are you sure to delete?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                try{
                    AppointmentFragment.mSQLiteHelper.deleteData(idRecord);
//                    Toast.makeText(this,"Delete successfully",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Log.e("error",e.getMessage());
                }
                updateRecordList();
            }
        });
        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void showDialogUpdate(final Activity activity, final int position){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_dialog);
        dialog.setTitle("Update");


        final EditText edtName=dialog.findViewById(R.id.edtName);
        final EditText edtDetail=dialog.findViewById(R.id.edtDetail);
        final EditText edtNote=dialog.findViewById(R.id.edtNote);
        final EditText edtDate=dialog.findViewById(R.id.edtDate);
        final EditText edtTime=dialog.findViewById(R.id.edtTime);


        Button btnDatePicker=dialog.findViewById(R.id.btn_date);
        Button btnTimePicker=dialog.findViewById(R.id.btn_time);
        Button btnUpdate=dialog.findViewById(R.id.update);

        Cursor cursor = AppointmentFragment.mSQLiteHelper.getData("SELECT * FROM RECORD WHERE id="+position);
        mList.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            edtName.setText(name);
            String detail = cursor.getString(2);
            edtDetail.setText(detail);
            String date = cursor.getString(3);
            edtDate.setText(date);
            String time = cursor.getString(4);
            edtTime.setText(time);
            String note = cursor.getString(5);
            edtNote.setText(note);

            mList.add(new Model(id,name,detail,date,time,note));
        }


        int width = (int)(activity.getResources().getDisplayMetrics().widthPixels*0.95);
        int height = (int)(activity.getResources().getDisplayMetrics().heightPixels*0.7);
        dialog.getWindow().setLayout(width,height);
        dialog.show();

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                edtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(activity,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                edtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    AppointmentFragment.mSQLiteHelper.updateData(
                            edtName.getText().toString().trim(),
                            edtDetail.getText().toString().trim(),
                            edtDate.getText().toString().trim(),
                            edtTime.getText().toString().trim(),
                            edtNote.getText().toString().trim(),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Update successful",Toast.LENGTH_SHORT).show();
                }
                catch (Exception error){
                    Log.e("Update error",error.getMessage());
                }
                updateRecordList();
            }
        });


    }

    private void updateRecordList() {

        Cursor cursor=AppointmentFragment.mSQLiteHelper.getData("SELECT * FROM RECORD");
        mList.clear();
        while(cursor.moveToNext()){
            int id=cursor.getInt(0);
            String name=cursor.getString(1);
            String detail=cursor.getString(2);
            String date=cursor.getString(3);
            String time=cursor.getString(4);
            String note=cursor.getString(5);

            mList.add(new Model(id,name,detail,date,time,note));
        }
        mAdapter.notifyDataSetChanged();
    }

}


