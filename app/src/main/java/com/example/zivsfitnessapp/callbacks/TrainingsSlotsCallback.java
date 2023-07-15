package com.example.zivsfitnessapp.callbacks;

import com.example.zivsfitnessapp.models.TrainingSlot;

import java.util.List;

public interface TrainingsSlotsCallback {
    void onDone(List<TrainingSlot> slots);
}
