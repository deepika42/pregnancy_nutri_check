package com.example.pregnancynutricheck;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CalendarActivity extends AppCompatActivity {
    TextView textView;
    public static final String RESULT = "result";
    public static final String EVENT = "event";
    private static final int ADD_NOTE = 44;

    private CalendarView mCalendarView;
    private List<EventDay> mEventDays = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);



        mCalendarView = (CalendarView) findViewById(R.id.calendarView);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });

        mCalendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                previewNote(eventDay);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE && resultCode == RESULT_OK) {
            MyEventDay myEventDay = data.getParcelableExtra(RESULT);
            mCalendarView.setDate(myEventDay.getCalendar());
            mEventDays.add(myEventDay);
            mCalendarView.setEvents(mEventDays);
        }
    }

    private void addNote() {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivityForResult(intent, ADD_NOTE);
    }

    private void previewNote(EventDay eventDay) {
        Intent intent = new Intent(this, NotePreviewActivity.class);
        if(eventDay instanceof MyEventDay){
            intent.putExtra(EVENT, (MyEventDay) eventDay);
        }
        startActivity(intent);
    }




}

