package com.example.zivsfitnessapp.screens;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zivsfitnessapp.FirebaseDataBaseManager;
import com.example.zivsfitnessapp.R;
import com.example.zivsfitnessapp.SessionManager;
import com.example.zivsfitnessapp.adapters.FindPersonalTrainerAdapter;
import com.example.zivsfitnessapp.callbacks.OnPersonalTrainerCtaClickListener;
import com.example.zivsfitnessapp.customviews.MyToolbarView;
import com.example.zivsfitnessapp.models.MemberShip;
import com.example.zivsfitnessapp.models.MembershipStatus;
import com.example.zivsfitnessapp.models.PersonalTrainer;
import com.example.zivsfitnessapp.models.PersonalTrainerData;
import com.example.zivsfitnessapp.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FindPersonalTrainerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Location location;
    List<PersonalTrainerData> personalTrainerDataList = new ArrayList<>();
    FindPersonalTrainerAdapter adapter;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        MyToolbarView toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setText(R.string.find_personal_trainers);
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FindPersonalTrainerAdapter();
        adapter.activity = this;
        adapter.listener = new OnPersonalTrainerCtaClickListener() {
            @Override
            public void onPersonalTrainerCtaClicked(int position, MembershipStatus membershipStatus) {
                if (membershipStatus == MembershipStatus.APPROVED) {
                    goToScheduleScreen(personalTrainerDataList.get(position).getPersonalTrainer());
                } else if (membershipStatus == MembershipStatus.NONE) {
                    PersonalTrainer personalTrainer =  personalTrainerDataList.get(position).getPersonalTrainer();
                    personalTrainerDataList.get(position).getPersonalTrainer().getMembersShip().add(new MemberShip(new User(SessionManager.client.getUid(), SessionManager.client.getFullName(), null, SessionManager.client.getImageUrl()), MembershipStatus.PENDING));
                    SessionManager.client.getMembersShip().add(new MemberShip(new User(personalTrainer.getUid(), personalTrainer.getFullName(), personalTrainer.getCompanyName(), personalTrainer.getImageUrl()), MembershipStatus.PENDING));
                    FirebaseDataBaseManager.createNewPendingRequest(SessionManager.client,  personalTrainerDataList.get(position).getPersonalTrainer());
                    adapter.notifyItemChanged(position);
                }
            }
        };
        adapter.personalTrainerDataList = personalTrainerDataList;
        recyclerView.setAdapter(adapter);
        findMyLocationIfHasAccess();
    }

    private void goToScheduleScreen(PersonalTrainer personalTrainer) {
        Intent intent = new Intent(this, ScheduleTrainingActivity.class);
        intent.putExtra("personalTrainer", personalTrainer);
        startActivity(intent);
    }


    private void fetchPersonalTrainers() {
        FirebaseDataBaseManager.getPersonalTrainersList(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    for (DocumentSnapshot document : documents) {
                        PersonalTrainer personalTrainer = document.toObject(PersonalTrainer.class);
                        personalTrainerDataList.add(new PersonalTrainerData(personalTrainer, getDistance(personalTrainer)));
                    }
                    sortPersonalTrainersList();
                    adapter.notifyDataSetChanged();
                } else {

                }
            }
        });
    }

    private float getDistance(PersonalTrainer personalTrainer) {
        float distance = -1;

        Location personalTrainerLocation = new Location("location");
        personalTrainerLocation.setLatitude(personalTrainer.getLocation().latitude);
        personalTrainerLocation.setLongitude(personalTrainer.getLocation().longitude);
        if (this.location != null) {
            distance = this.location.distanceTo(personalTrainerLocation) / 1000;
        }

        return distance;
    }


    private void sortPersonalTrainersList() {
        personalTrainerDataList.sort(new Comparator<PersonalTrainerData>() {
            @Override
            public int compare(PersonalTrainerData o1, PersonalTrainerData o2) {
                return Float.compare(o1.getDistance(), o2.getDistance());
            }
        });

    }

    private void findMyLocationIfHasAccess() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            findMyLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                findMyLocation();
            }
        }
    }

    private void findMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful()) {
                                FindPersonalTrainerActivity.this.location = task.getResult();
                            }
                            fetchPersonalTrainers();
                        }

                    });
        }
    }

}