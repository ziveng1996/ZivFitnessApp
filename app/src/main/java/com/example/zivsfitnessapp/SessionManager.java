package com.example.zivsfitnessapp;

import com.example.zivsfitnessapp.models.Client;
import com.example.zivsfitnessapp.models.MemberShip;
import com.example.zivsfitnessapp.models.PersonalTrainer;
import com.example.zivsfitnessapp.models.User;

import java.util.List;

public class SessionManager {
    public static String userUid;
    public static Client client;
    public static PersonalTrainer personalTrainer;

    public static boolean isPersonalTrainer() {
        return personalTrainer != null;
    }

    public static List<MemberShip> getMembersShip() {
        return client == null ? personalTrainer.getMembersShip() : client.getMembersShip();
    }

    public static User getCurrentUser() {
        return client == null ? personalTrainer : client;
    }

    public static void clear() {
        userUid = null;
        client = null;
        personalTrainer = null;
    }
}
