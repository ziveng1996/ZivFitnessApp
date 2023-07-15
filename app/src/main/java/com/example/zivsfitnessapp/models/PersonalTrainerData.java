package com.example.zivsfitnessapp.models;

public class PersonalTrainerData {

    private float distance;
    private PersonalTrainer personalTrainer;

    public PersonalTrainerData(PersonalTrainer personalTrainer, float distance) {
        this.distance = distance;
        this.personalTrainer = personalTrainer;

    }
    public float getDistance() {
        return distance;
    }

    public PersonalTrainer getPersonalTrainer() {
        return personalTrainer;
    }
}
