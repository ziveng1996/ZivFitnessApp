package com.example.zivsfitnessapp.screens.sharedscreens;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zivsfitnessapp.FirebaseDataBaseManager;
import com.example.zivsfitnessapp.R;
import com.example.zivsfitnessapp.SessionManager;
import com.example.zivsfitnessapp.adapters.PendingRequestsAdapter;
import com.example.zivsfitnessapp.callbacks.PendingRequestCallback;
import com.example.zivsfitnessapp.customviews.MyToolbarView;
import com.example.zivsfitnessapp.models.MemberShip;
import com.example.zivsfitnessapp.models.MembershipStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PendingRequestsActivity extends AppCompatActivity {

    List<MemberShip> items = new ArrayList<>();
    PendingRequestsAdapter adapter = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        MyToolbarView toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setText(R.string.my_pending_request);

        RecyclerView list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        items = SessionManager.getMembersShip().stream().filter(new Predicate<MemberShip>() {
            @Override
            public boolean test(MemberShip memberShip) {
                return memberShip.status == MembershipStatus.PENDING;
            }
        }).collect(Collectors.toList());

        adapter = new PendingRequestsAdapter(items, new PendingRequestCallback() {
            @Override
            public void onClicked(MembershipStatus status, int position, MemberShip memberShip) {
                memberShip.status = status;
                FirebaseDataBaseManager.updateMembership(items.get(position).user, SessionManager.personalTrainer, status);
                adapter.notifyItemChanged(position);
            }
        });
        list.setAdapter(adapter);
    }
}
