package com.example.zivsfitnessapp.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PersonalTrainer extends User {

    public PersonalTrainer(String uid, String fullName, String imageUrl, String companyName, Location location, List<DailyOpeningHours> openningHours) {
        super(uid, fullName, imageUrl);
        this.companyName = companyName;
        this.location = location;
        this.openingHours = openningHours;
        this.trainings = new HashMap<>();
        this.membersShip = new ArrayList<>();
    }

    public PersonalTrainer() {
        super();
    }

    private List<DailyOpeningHours> openingHours;
    private Location location;

    public HashMap<String, List<Training>> getTrainings() {
        return trainings;
    }

    private HashMap<String, List<Training>> trainings;

    public List<DailyOpeningHours> getOpeningHours() {
        return openingHours;
    }

    public Location getLocation() {
        return location;
    }
}
