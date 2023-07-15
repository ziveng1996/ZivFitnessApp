package com.example.zivsfitnessapp.callbacks;

import com.example.zivsfitnessapp.models.MemberShip;
import com.example.zivsfitnessapp.models.MembershipStatus;

public interface PendingRequestCallback {
    void onClicked(MembershipStatus status, int position, MemberShip memberShip);
}
