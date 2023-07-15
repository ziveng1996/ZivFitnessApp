package com.example.zivsfitnessapp.screens;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zivsfitnessapp.R;
import com.example.zivsfitnessapp.SessionManager;
import com.example.zivsfitnessapp.Utils;
import com.example.zivsfitnessapp.adapters.TrainingsAdapter;
import com.example.zivsfitnessapp.customviews.MyToolbarView;
import com.example.zivsfitnessapp.models.PersonalTrainer;
import com.example.zivsfitnessapp.models.Training;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

public class PersonalTrainerTrainingsActivity extends AppCompatActivity {

    private PersonalTrainer personalTrainer = SessionManager.personalTrainer;
    private long currentDate = Utils.getTodayDate();

    private AppCompatTextView dateTV;
    private RecyclerView recyclerView;
    private TrainingsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_trainer_training_item_activity);
        MyToolbarView toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setText(R.string.my_trainings);

        dateTV = findViewById(R.id.date);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TrainingsAdapter();
        recyclerView.setAdapter(adapter);

        dateTV.setOnClickListener(v -> {
            openCalendar();
        });

        onDateSelected(currentDate);
    }

    private void openCalendar() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(currentDate);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog pickerDialog = new DatePickerDialog(this, (view, y, m, d) -> {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            cal.setTimeInMillis(0);
            cal.set(Calendar.DAY_OF_MONTH, d);
            cal.set(Calendar.MONTH, m);
            cal.set(Calendar.YEAR, y);
            onDateSelected(cal.getTimeInMillis());
        }, year, month, day);

        pickerDialog.show();
    }

    private void onDateSelected(long date) {
        currentDate = date;
        dateTV.setText(Utils.dateToStr(date));
        List<Training> trainings = personalTrainer.getTrainings().get(String.valueOf(date));
        if (trainings != null) {
            List<Training> items = new ArrayList<>(trainings);
            Collections.sort(items);
            adapter.setTrainings(items);
        } else {
            adapter.setTrainings(new ArrayList<>());
        }
    }
}