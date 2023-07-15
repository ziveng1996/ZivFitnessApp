package com.example.zivsfitnessapp.models;

import java.io.Serializable;

public class Training implements Serializable, Comparable<Training> {

    public Training(Long date, int hour, User client, User personalTrainer) {
        this.date = date;
        this.hour = hour;
        this.client = client;
        this.personalTrainer = personalTrainer;
    }

    public Training() {

    }

    Long date;
    int hour;
    User client;
    User personalTrainer;


    public Long getDate() {
        return date;
    }

    public int getHour() {
        return hour;
    }

    public User getClient() {
        return client;
    }

    public User getPersonalTrainer() {
        return personalTrainer;
    }

    @Override
    public int compareTo(Training o) {
        return Long.compare(date + hour, o.getDate() + o.getHour());
    }
}
