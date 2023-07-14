package com.example.zivsfitnessapp.screens.sharedscreens;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.location.Address;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.zivsfitnessapp.FirebaseDataBaseManager;
import com.example.zivsfitnessapp.R;
import com.example.zivsfitnessapp.SessionManager;
import com.example.zivsfitnessapp.customviews.DayOpeningHoursView;
import com.example.zivsfitnessapp.customviews.LoadingView;
import com.example.zivsfitnessapp.models.Client;
import com.example.zivsfitnessapp.models.DailyOpeningHours;
import com.example.zivsfitnessapp.models.Location;
import com.example.zivsfitnessapp.models.PersonalTrainer;
import com.example.zivsfitnessapp.screens.FindMyLocationActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class CreateProfileActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_PHOTO_GALLERY = 2;
    static final int REQUEST_LOCATION = 3;

    private TextInputEditText fullNameET;
    private TextInputLayout companyContainer;
    private TextInputEditText companyET;
    private TextInputLayout locationContainer;
    private TextInputEditText locationET;
    private AppCompatImageView imageView;
    private MaterialButton takePhotoButton;
    private MaterialButton galleryButton;
    private DayOpeningHoursView sunday;
    private DayOpeningHoursView monday;
    private DayOpeningHoursView tuesday;
    private DayOpeningHoursView wednesday;
    private DayOpeningHoursView thursday;
    private DayOpeningHoursView friday;
    private MaterialButton saveButton;
    private Bitmap imageBitmap;
    private RadioGroup radioGroup;
    private RadioButton clientRB;
    private RadioButton personalTrainerRB;
    private LinearLayout openingHoursContainer;
    private LoadingView loadingView;
    private StorageReference storageRef;

    private Address address;
    private String imageDownloadUrl;

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                    imageBitmap = photo;
                    imageView.setImageBitmap(photo);
                }
            }
    );

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    try {
                        Uri uri = result.getData().getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        imageBitmap = bitmap;
                        imageView.setImageBitmap(bitmap);
                    } catch (Exception e) {
                    }
                }
            }
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile_layout);
        radioGroup = findViewById(R.id.radioGroup);
        clientRB = findViewById(R.id.clientRB);
        personalTrainerRB = findViewById(R.id.personalTrainerRB);
        fullNameET = findViewById(R.id.fullNameET);
        companyET = findViewById(R.id.companyET);
        companyContainer = findViewById(R.id.companyContainer);
        locationContainer = findViewById(R.id.locationContainer);
        locationET = findViewById(R.id.locationET);
        loadingView = findViewById(R.id.loading);
        imageView = findViewById(R.id.imageView);
        takePhotoButton = findViewById(R.id.takePhoto);
        galleryButton = findViewById(R.id.addFromGallery);
        saveButton = findViewById(R.id.save);
        openingHoursContainer = findViewById(R.id.openingHoursContainer);

        sunday = findViewById(R.id.sunday);
        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);

        sunday.setDay(getString(R.string.sunday));
        monday.setDay(getString(R.string.monday));
        tuesday.setDay(getString(R.string.tuesday));
        wednesday.setDay(getString(R.string.wednesday));
        thursday.setDay(getString(R.string.thursday));
        friday.setDay(getString(R.string.friday));

        saveButton.setOnClickListener(v -> {
            onSaveClicked();
        });

        takePhotoButton.setOnClickListener(v -> {
            openCamera();
        });

        galleryButton.setOnClickListener(v -> {
            openPhotoGallery();
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.personalTrainerRB) {
                openingHoursContainer.setVisibility(View.VISIBLE);
                companyContainer.setVisibility(View.VISIBLE);
                locationContainer.setVisibility(View.VISIBLE);
            } else {
                openingHoursContainer.setVisibility(View.GONE);
                companyContainer.setVisibility(View.GONE);
                locationContainer.setVisibility(View.GONE);
            }
        });
        radioGroup.check(R.id.clientRB);

        locationET.setOnClickListener(v -> {
            Intent intent = new Intent(this, FindMyLocationActivity.class);
            startActivityForResult(intent, REQUEST_LOCATION);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_LOCATION) {
            address = (Address) data.getParcelableExtra("address");
            locationET.setText(address.getAddressLine(0));
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraActivityResultLauncher.launch(takePictureIntent);
    }

    private void openPhotoGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        galleryActivityResultLauncher.launch(intent);
    }

    private void uploadImageToStorage(Runnable onDone) {
        // the image name should be the user uuid - uniqe
        StorageReference imageRef = storageRef.child("images/" + SessionManager.userUid + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();
        // Upload file to Firebase Storage
        UploadTask uploadTask = imageRef.putBytes(imageData);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                imageDownloadUrl = uri.toString();
                onDone.run();
            });
        }).addOnFailureListener(e -> {
            // Handle failed upload
            Log.e("TAG", "Upload failed: " + e.getMessage());
        });
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        Bitmap bitmap = null;
        ContentResolver contentResolver = this.getContentResolver();
        try {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
            } else {
                ImageDecoder.Source source = ImageDecoder.createSource(contentResolver, uri);
                bitmap = ImageDecoder.decodeBitmap(source);
            }
        } catch (Exception e) {
        }

        return bitmap;
    }

    private void onSaveClicked() {
        if (checkValidInput()) {
            loadingView.setVisibility(View.VISIBLE);
            uploadImageToStorage(() -> {
                if (radioGroup.getCheckedRadioButtonId() == R.id.clientRB) {
                    createClientProfile();
                } else {
                    createPersonalTrainerProfile();
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.create_profile_invalid_message), Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkValidInput() {
        if (radioGroup.getCheckedRadioButtonId() == R.id.clientRB) {
            return !TextUtils.isEmpty(fullNameET.getText())
                    && imageBitmap != null;
        } else {
            return !TextUtils.isEmpty(fullNameET.getText())
                    && !TextUtils.isEmpty(companyET.getText())
                    && imageBitmap != null
                    && address != null;
        }
    }

    private void createClientProfile() {
        Client client = new Client(
                SessionManager.userUid,
                fullNameET.getText().toString(),
                imageDownloadUrl);

        FirebaseDataBaseManager.createClientProfile(client, () -> {
            SessionManager.client = client;
            goToClientScreen();
        });
    }

    private void createPersonalTrainerProfile() {
        PersonalTrainer personalTrainer = new PersonalTrainer(
                SessionManager.userUid,
                fullNameET.getText().toString(),
                imageDownloadUrl,
                companyET.getText().toString(),
                new Location(address.getLatitude(), address.getLongitude()),
                new ArrayList(Arrays.asList(
                        new DailyOpeningHours(sunday.getFrom(), sunday.getTo()),
                        new DailyOpeningHours(monday.getFrom(), monday.getTo()),
                        new DailyOpeningHours(tuesday.getFrom(), tuesday.getTo()),
                        new DailyOpeningHours(wednesday.getFrom(), wednesday.getTo()),
                        new DailyOpeningHours(thursday.getFrom(), thursday.getTo()),
                        new DailyOpeningHours(friday.getFrom(), friday.getTo())
                ))
        );

        FirebaseDataBaseManager.createPersonalTrainerProfile(personalTrainer, () -> {
            SessionManager.personalTrainer = personalTrainer;
            goToClientScreen();
        });
    }

    private void goToClientScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
