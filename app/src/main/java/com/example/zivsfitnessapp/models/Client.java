package com.example.zivsfitnessapp.models;

import java.util.ArrayList;
import java.util.List;

public class Client extends User {

    public Client(String uid, String fullName, String imageUrl) {
        super(uid, fullName, imageUrl);
        this.trainings = new ArrayList<>();
        this.membersShip = new ArrayList<>();
    }

    public Client() {
        super();
    }

    public List<Training> trainings;
}

