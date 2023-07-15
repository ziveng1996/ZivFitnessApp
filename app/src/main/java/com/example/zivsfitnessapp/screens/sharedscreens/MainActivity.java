package com.example.zivsfitnessapp.screens.sharedscreens;

import static com.example.zivsfitnessapp.R.*;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.zivsfitnessapp.SessionManager;
import com.example.zivsfitnessapp.screens.ClientTrainingsActivity;
import com.example.zivsfitnessapp.screens.FindPersonalTrainerActivity;
import com.example.zivsfitnessapp.screens.PersonalTrainerTrainingsActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted && this.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    Snackbar.make(
                            MainActivity.this.findViewById(id.content).getRootView(),
                            MainActivity.this.getString(string.please_grant_notification_permission_from_app_settings),
                            Snackbar.LENGTH_LONG).show();
                }});

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(layout.main_activity);
        ImageView imageView = findViewById(id.image);

        if (SessionManager.getCurrentUser().getImageUrl() != null) {
            Glide.with(this)
                    .load(SessionManager.getCurrentUser().getImageUrl())
                    .centerCrop()
                    .placeholder(drawable.baseline_supervisor_account_24)
                    .into(imageView);
        }

        AppCompatTextView companyNameTV = findViewById(id.company_name);
        companyNameTV.setText(SessionManager.getCurrentUser().getCompanyName());
        if (companyNameTV.getText().length() == 0 ){
            companyNameTV.setVisibility(View.GONE);
        }

        AppCompatTextView nameTV = findViewById(id.name);
        nameTV.setText(SessionManager.getCurrentUser().getFullName());
        MaterialButton scheduleBT = findViewById(id.schedule_training);
        scheduleBT.setOnClickListener(v -> goToFindPersonalTrainersActivity());
        if (SessionManager.isPersonalTrainer()) {
            scheduleBT.setVisibility(View.GONE);
        }

        MaterialButton myTrainingsBT = findViewById(id.my_trainings);
        myTrainingsBT.setOnClickListener(v -> goToTrainingsScreen());

        MaterialButton signOutBT = findViewById(id.sign_out);
        signOutBT.setOnClickListener(v -> signOut());

        MaterialButton myPendingRequestBT = findViewById(id.pending_requests);
        myPendingRequestBT.setOnClickListener(v -> goToPendingRequestsScreen());

        MaterialButton membersShipBT = findViewById(id.members_ship);
        membersShipBT.setOnClickListener(v -> goToMembersShipScreen(membersShipBT.getText().toString()));
        membersShipBT.setText(SessionManager.isPersonalTrainer() ? string.my_clients : string.my_personal_trainers);
    }

    private void askForNotificationsPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void goToPendingRequestsScreen() {
        Intent intent = new Intent(MainActivity.this, PendingRequestsActivity.class);
        startActivity(intent);
    }

    private void goToMembersShipScreen(String title) {
        Intent intent = new Intent(MainActivity.this, MembersShipActivity.class);
        intent.putExtra(MembersShipActivity.TITLE,title);
        startActivity(intent);
    }

    private void goToFindPersonalTrainersActivity() {
        Intent intent = new Intent(this, FindPersonalTrainerActivity.class);
        startActivity(intent);
    }

    private void goToTrainingsScreen() {
        if (SessionManager.isPersonalTrainer()) {
            startActivity(new Intent(this, PersonalTrainerTrainingsActivity.class));
        } else {
            startActivity(new Intent(this, ClientTrainingsActivity.class));
        }
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        SessionManager.clear();
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }
}
