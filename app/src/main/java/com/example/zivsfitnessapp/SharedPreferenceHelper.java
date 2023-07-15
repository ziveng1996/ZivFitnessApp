//package com.example.zivsfitnessapp;
//
//import static android.content.Context.MODE_PRIVATE;
//
//import android.app.Activity;
//import android.content.SharedPreferences;
//
//import java.util.ArrayList;
//
//public class SharedPreferenceHelper {
//
//    public SharedPreferenceHelper() {
//
//    }
//    public static SharedPreferenceHelper getInstance() {
//        return new SharedPreferenceHelper();
//    }
//    public void uploadTraining(Activity activity, Training training) {
//        SharedPreferences.Editor editor = activity.getSharedPreferences(training.getId(), MODE_PRIVATE).edit();
//        training.uploadTraining(editor);
//    }
//
//    public ArrayList<Training> getTrainingList(Activity activity) {
//        ArrayList<Training> trainings = new ArrayList<>();
//        SharedPreferences prefs = activity.getSharedPreferences(Training.ID_COUNT_KEY, MODE_PRIVATE);
//        int idCount = prefs.getInt(Training.ID_COUNT_KEY,0);
//        for(int i = Training.START_ID_COUNT; i<idCount; i++) {
//            prefs = activity.getSharedPreferences(Training.START_ID+i,MODE_PRIVATE);
//            trainings.add(new Training(prefs));
//        }
//        return trainings;
//    }
//}
