package com.example.zivsfitnessapp.models;

import java.io.Serializable;

public class MemberShip implements Serializable {
    public MembershipStatus status = MembershipStatus.NONE;
    public User user;

    public MemberShip(User user, MembershipStatus status) {
        this.status = status;
        this.user = user;
    }

    public MemberShip(){}
}