package com.example.zivsfitnessapp.screens;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zivsfitnessapp.R;
import com.example.zivsfitnessapp.SessionManager;
import com.example.zivsfitnessapp.adapters.TrainingsAdapter;
import com.example.zivsfitnessapp.customviews.MyToolbarView;
import com.example.zivsfitnessapp.models.Client;
import com.example.zivsfitnessapp.models.Training;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientTrainingsActivity extends AppCompatActivity {

    private Client client = SessionManager.client;

    private RecyclerView recyclerView;
    private TrainingsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        MyToolbarView toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setText(R.string.my_trainings);

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TrainingsAdapter();
        recyclerView.setAdapter(adapter);

        if (client.trainings != null) {
            List<Training> items = new ArrayList(client.trainings);
            Collections.sort(items);
            adapter.setTrainings(items);
        }
    }
}
