package com.example.zivsfitnessapp.screens.sharedscreens;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zivsfitnessapp.R;
import com.example.zivsfitnessapp.SessionManager;
import com.example.zivsfitnessapp.adapters.MembersShipAdapter;
import com.example.zivsfitnessapp.customviews.MyToolbarView;
import com.example.zivsfitnessapp.models.MemberShip;
import com.example.zivsfitnessapp.models.MembershipStatus;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MembersShipActivity extends AppCompatActivity {

    public static final String TITLE= "Title";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra(TITLE);
        setContentView(R.layout.list_layout);
        MyToolbarView toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setText(title);


        RecyclerView rv = findViewById(R.id.list);
        List<MemberShip> approvedMembersShip = SessionManager.getMembersShip().stream().filter(new Predicate<MemberShip>() {
            @Override
            public boolean test(MemberShip MemberShip) {
                return MemberShip.status == MembershipStatus.APPROVED;
            }
        }).collect(Collectors.toList());
        MembersShipAdapter adapter = new MembersShipAdapter(approvedMembersShip);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }
}
