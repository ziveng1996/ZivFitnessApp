package com.example.zivsfitnessapp;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;

public class Training {
    private String id;
    private String name;
    private int roomNumber;
    private String trainerName;
    private int hours;
    private int minutes;
    private float calories;
    private String date;

    public static final String ID_COUNT_KEY = "IDS";
    public static final String ID_KEY = "id";
    public static final String NAME_KEY = "name";
    public static final String ROOM_NUMBER_KEY = "roomNumber";
    public static final String TRAINER_NAME_KEY = "trainerName";
    public static final String HOURS_KEY = "hours";
    public static final String MINUTES_KEY = "minutes";
    public static final String CALORIES_KEY = "calories";
    public static final String DATE_KEY = "date";

    public static final int START_ID_COUNT = 1;
    public static final String START_ID = "T";

    public Training(Activity activity, String name, int roomNumber, String trainerName, int hours, int minutes, float calories, String date) {
        this.id = getNewId(activity);
        this.name = name;
        this.roomNumber = roomNumber;
        this.trainerName = trainerName;
        this.hours = hours;
        this.minutes = minutes;
        this.calories = calories;
        this.date = date;
    }

    public Training(SharedPreferences sharedPreference) {
        this.id = sharedPreference.getString(ID_KEY,"");
        this.name = sharedPreference.getString(NAME_KEY,"");
        this.roomNumber = sharedPreference.getInt(ROOM_NUMBER_KEY,0);
        this.trainerName = sharedPreference.getString(TRAINER_NAME_KEY,"");
        this.hours = sharedPreference.getInt(HOURS_KEY,0);
        this.minutes = sharedPreference.getInt(MINUTES_KEY,0);
        this.calories = sharedPreference.getFloat(CALORIES_KEY, 0.9F);
        this.date = sharedPreference.getString(DATE_KEY,"");
    }

    private String getNewId(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(ID_COUNT_KEY,MODE_PRIVATE);
        int id = sharedPreferences.getInt(ID_COUNT_KEY, START_ID_COUNT);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt(ID_COUNT_KEY,id+1).commit();
        return START_ID+id;
    }

    public void uploadTraining(SharedPreferences.Editor editor) {
        editor.putString(ID_KEY,this.id);
        editor.putString(NAME_KEY,this.name);
        editor.putInt(ROOM_NUMBER_KEY,this.roomNumber);
        editor.putString(TRAINER_NAME_KEY,this.trainerName);
        editor.putInt(HOURS_KEY,this.hours);
        editor.putInt(MINUTES_KEY,this.minutes);
        editor.putFloat(CALORIES_KEY,this.calories);
        editor.putString(DATE_KEY,this.date);
        editor.commit();
    }

    public String getFormattedDuration() {
        String formattedHours = String.valueOf(this.hours);
        if(formattedHours.length()<2)
            formattedHours = "0" + formattedHours;

        String formattedMinutes = String.valueOf(this.hours);
        if(formattedMinutes.length()<2)
            formattedMinutes = "0" + formattedMinutes;

        return formattedHours + ":" + formattedMinutes;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public String getDate() {
        return date;
    }
}
