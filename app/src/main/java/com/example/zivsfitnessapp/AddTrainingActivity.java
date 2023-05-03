package com.example.zivsfitnessapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class AddTrainingActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etName, etRoomNumber, etTrainerName, etHours, etMinutes, etCalories;
    Button btnSave;
    DatePicker dpDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        initWidgets();
    }

    private void initWidgets() {
        etName = findViewById(R.id.etName);
        etRoomNumber = findViewById(R.id.etRoomNumber);
        etTrainerName = findViewById(R.id.etTrainerName);
        etHours = findViewById(R.id.etHours);
        etMinutes = findViewById(R.id.etMinutes);
        etCalories = findViewById(R.id.etCalories);
        dpDate = findViewById(R.id.dpDate);

        Date date = new Date(new Date().getTime());
        dpDate.setMaxDate(date.getTime());

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
    }

    private void uploadTraining(String name, int roomNumber, String trainerName, int hours, int minutes, float calories) {
        SharedPreferenceHelper.getInstance().uploadTraining(this, new Training(this, name.trim(), roomNumber, trainerName.trim(), hours, minutes, calories, getFormattedDate()));
        Toast.makeText(this, "The data saved :)", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String getFormattedDate() {
        return "" + dpDate.getDayOfMonth() + "/" + dpDate.getMonth() + "/" + dpDate.getYear();
    }

    private boolean isThereEmptyData() {
        return etName.getText().toString().trim().isEmpty() ||
                etRoomNumber.getText().toString().trim().isEmpty() ||
                etTrainerName.getText().toString().trim().isEmpty() ||
                etHours.getText().toString().trim().isEmpty() ||
                etMinutes.getText().toString().trim().isEmpty() ||
                etCalories.getText().toString().trim().isEmpty();
    }

    private boolean isTimeCurrent(int minutes) {
        return minutes < 60;
    }

    @Override
    public void onClick(View view) {
        if (view == btnSave) {
            if (isThereEmptyData()) {
                Toast.makeText(this, "Insert all Data!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!isTimeCurrent(Integer.parseInt(etMinutes.getText().toString().trim()))) {
                Toast.makeText(this, "Insert up to 59 minutes!", Toast.LENGTH_SHORT).show();
                return;
            }

            uploadTraining(etName.getText().toString().trim(),
                    Integer.parseInt(etRoomNumber.getText().toString().trim()),
                    etTrainerName.getText().toString().trim(),
                    Integer.parseInt(etHours.getText().toString().trim()),
                    Integer.parseInt(etMinutes.getText().toString().trim()),
                    Float.parseFloat(etCalories.getText().toString().trim()));
        }
    }

}