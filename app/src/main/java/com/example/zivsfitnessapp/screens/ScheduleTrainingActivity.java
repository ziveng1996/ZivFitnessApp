package com.example.zivsfitnessapp.screens;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zivsfitnessapp.FirebaseDataBaseManager;
import com.example.zivsfitnessapp.R;
import com.example.zivsfitnessapp.Utils;
import com.example.zivsfitnessapp.adapters.TrainingSlotAdapter;
import com.example.zivsfitnessapp.customviews.LoadingView;
import com.example.zivsfitnessapp.customviews.MyToolbarView;
import com.example.zivsfitnessapp.models.PersonalTrainer;
import com.example.zivsfitnessapp.models.TrainingSlot;

import java.util.Calendar;
import java.util.TimeZone;

public class ScheduleTrainingActivity extends AppCompatActivity {

    private PersonalTrainer personalTrainer;
    private AppCompatImageView image;
    private AppCompatTextView dateTV;
    private AppCompatTextView company;
    private AppCompatTextView name;
    private RecyclerView recyclerView;
    private TrainingSlotAdapter adapter;
    private LoadingView loadingView;
    private long currentDate = Utils.getTodayDate();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_training);

        personalTrainer = (PersonalTrainer) getIntent().getSerializableExtra("personalTrainer");

        MyToolbarView toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(v -> finish());

        toolbar.setText(R.string.schedule_training);

        image = findViewById(R.id.image);
        loadingView = findViewById(R.id.loading);
        Glide.with(this)
                .load(personalTrainer.getImageUrl())
                .circleCrop()
                .placeholder(R.drawable.baseline_supervisor_account_24)
                .into(image);
        dateTV = findViewById(R.id.date);
        company = findViewById(R.id.company);
        company.setText(personalTrainer.getCompanyName());
        name = findViewById(R.id.name);
        name.setText(personalTrainer.getFullName());
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new TrainingSlotAdapter();
        adapter.callback = new TrainingSlotAdapter.Callback() {
            @Override
            public void onSlotClicked(TrainingSlot slot) {
                scheduleTraining(slot);

            }
        };

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
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

        pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        pickerDialog.show();
    }

    private void onDateSelected(long date) {
        currentDate = date;
        dateTV.setText(Utils.dateToStr(date));
        FirebaseDataBaseManager.getPersonalTrainerSlots(personalTrainer.getUid(), date, slots -> {
            adapter.setSlots(slots);
        });
    }

    private void scheduleTraining(TrainingSlot slot) {
        loadingView.setVisibility(View.VISIBLE);
        FirebaseDataBaseManager.scheduledTraining(
                personalTrainer,
                currentDate,
                slot.getHour(),
                () -> {
                    loadingView.setVisibility(View.GONE);
                    Toast.makeText(ScheduleTrainingActivity.this, getString(R.string.training_scheduled_successfully), Toast.LENGTH_SHORT).show();
                    finish();
                });
    }
}
