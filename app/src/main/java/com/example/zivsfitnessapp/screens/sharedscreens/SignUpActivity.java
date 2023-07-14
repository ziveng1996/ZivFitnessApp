package com.example.zivsfitnessapp.screens.sharedscreens;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.zivsfitnessapp.FirebaseDataBaseManager;
import com.example.zivsfitnessapp.R;
import com.example.zivsfitnessapp.SessionManager;
import com.example.zivsfitnessapp.customviews.LoadingView;
import com.example.zivsfitnessapp.models.Client;
import com.example.zivsfitnessapp.models.PersonalTrainer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout password;
    private TextInputLayout email;
    private MaterialButton signUpBT;
    private MaterialButton signInBT;
    private FirebaseAuth mAuth;
    private AppCompatTextView emailErrorMsg;
    private AppCompatTextView passErrorMsg;
    private LoadingView loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.sign_up_screen);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        signUpBT = findViewById(R.id.sign_up);
        signInBT = findViewById(R.id.sign_in);
        loading = findViewById(R.id.loading);

        signUpBT.setOnClickListener(v -> onSignUpButtonClicked(email.getEditText().getText().toString(), password.getEditText().getText().toString()));
        email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailErrorMsg.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        password.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passErrorMsg.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        signInBT.setOnClickListener(v -> onSignInButtonClicked(email.getEditText().getText().toString(), password.getEditText().getText().toString()));
        emailErrorMsg = findViewById(R.id.email_error);
        passErrorMsg = findViewById(R.id.pass_error);

    }

    private void onSignUpButtonClicked(String email, String pass) {
        if (checkValidation(email, pass)) {
            loading.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    loading.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        SessionManager.userUid = mAuth.getUid();
                        goToCreateProfileScreen();
                    } else {
                        showAlertDialog("Error", task.getException().getMessage());
                    }
                }
            });
        }
    }

    private void onSignInButtonClicked(String email, String pass) {
        if (checkValidation(email, pass)) {
            loading.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            loading.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                SessionManager.userUid = mAuth.getUid();
                                goToRelevantScreen(user.getDisplayName());
                            } else {
                                showAlertDialog("Error", task.getException().getMessage());
                            }
                        }
                    });
        }

    }


    private boolean checkValidation(String email, String pass) {
        boolean res = true;
        if (email == null || email.isEmpty()) {
            emailErrorMsg.setVisibility(View.VISIBLE);
            res = false;
        }
        if (pass == null || pass.isEmpty()) {
            passErrorMsg.setVisibility(View.VISIBLE);
            res = false;
        }

        return res;
    }


    private void goToMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void goToCreateProfileScreen() {
        Intent intent = new Intent(this, CreateProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToRelevantScreen(String userDisplayName) {
        if ("personalTrainer".equals(userDisplayName)) {
            FirebaseDataBaseManager.getPersonalTrainer(SessionManager.userUid, new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    SessionManager.personalTrainer = documentSnapshot.toObject(PersonalTrainer.class);
                    goToMainScreen();
                }
            });
        } else {
            FirebaseDataBaseManager.getClientUser(SessionManager.userUid, new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    SessionManager.client = documentSnapshot.toObject(Client.class);
                    goToMainScreen();

                }
            });
        }
    }
}