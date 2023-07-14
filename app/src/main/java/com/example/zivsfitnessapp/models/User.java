package com.example.zivsfitnessapp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    protected String fullName;
    protected String uid;
    protected String imageUrl;
    protected String companyName;
    protected List<MemberShip> membersShip = new ArrayList<>();

    public User(String uui, String fullName, String imageUrl) {
        this.uid = uui;
        this.fullName = fullName;
        this.imageUrl = imageUrl;
    }

    public User(String uui, String fullName, String companyName, String imageUrl) {
        this.uid = uui;
        this.fullName = fullName;
        this.imageUrl = imageUrl;
        this.companyName = companyName;
        this.membersShip = membersShip == null ? new ArrayList<>() : membersShip;

    }

    public User() {}

    public String getFullName() {
        return fullName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getUid() {
        return uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<MemberShip> getMembersShip() {
        return membersShip;
    }
}
