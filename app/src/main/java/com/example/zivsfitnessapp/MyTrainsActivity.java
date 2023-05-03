package com.example.zivsfitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyTrainsActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fBtnAdd;
    RecyclerView rvTrains;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trains);
        initWidgets();
    }

    private void initWidgets() {
        fBtnAdd = findViewById(R.id.fBtnAdd);
        fBtnAdd.setOnClickListener(this);

        rvTrains = findViewById(R.id.rvTrains);
        rvTrains.setHasFixedSize(false);
        rvTrains.setLayoutManager(new LinearLayoutManager(this));
        rvTrains.setAdapter(new TrainingAdapter(this,SharedPreferenceHelper.getInstance().getTrainingList(this)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        rvTrains.setAdapter(new TrainingAdapter(this,SharedPreferenceHelper.getInstance().getTrainingList(this)));
    }

    @Override
    public void onClick(View view) {
        if(view == fBtnAdd) {
            startActivity(new Intent(MyTrainsActivity.this, AddTrainingActivity.class));
        }
    }
}