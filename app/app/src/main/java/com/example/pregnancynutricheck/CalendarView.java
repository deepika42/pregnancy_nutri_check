package com.example.pregnancynutricheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.pregnancynutricheck.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;

public class CalendarView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

        MaterialCalendarView materialCalendarView=(MaterialCalendarView)findViewById(R.id.calendarView);
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(1900, 1, 1))
                .setMaximumDate(CalendarDay.from(2100, 1, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Toast.makeText(CalendarView.this,""+date,Toast.LENGTH_SHORT).show();

            }
        });
    }
}