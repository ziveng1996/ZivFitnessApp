package com.example.zivsfitnessapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.zivsfitnessapp.callbacks.TrainingsSlotsCallback;
import com.example.zivsfitnessapp.models.Client;
import com.example.zivsfitnessapp.models.DailyOpeningHours;
import com.example.zivsfitnessapp.models.MemberShip;
import com.example.zivsfitnessapp.models.MembershipStatus;
import com.example.zivsfitnessapp.models.PersonalTrainer;
import com.example.zivsfitnessapp.models.Training;
import com.example.zivsfitnessapp.models.TrainingSlot;
import com.example.zivsfitnessapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDataBaseManager {

    private static final String clientsCollection = "clients";
    private static final String personalTrainersCollection = "personalTrainers";

    public static void getPersonalTrainersList(OnCompleteListener<QuerySnapshot> listener) {
        FirebaseFirestore.getInstance().collection(personalTrainersCollection).get().addOnCompleteListener(listener);
    }

    public static void getClientUser(String uid, OnSuccessListener<DocumentSnapshot> listener) {
        FirebaseFirestore.getInstance().collection(clientsCollection).document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                listener.onSuccess(documentSnapshot);
                registerForUpdates(clientsCollection, uid, false);
            }
        });
    }

    public static void getPersonalTrainer(String id, OnSuccessListener<DocumentSnapshot> listener) {
        FirebaseFirestore
                .getInstance()
                .collection(personalTrainersCollection)
                .document(id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        listener.onSuccess(documentSnapshot);
                        registerForUpdates(personalTrainersCollection, id, true);
                    }
                });
    }


    private static void registerForUpdates(String collection, String uid, boolean isPersonalTrainer) {
        FirebaseFirestore.getInstance().collection(collection).document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (snapshot != null && snapshot.exists()) {
                    if (isPersonalTrainer) {
                        SessionManager.personalTrainer = snapshot.toObject(PersonalTrainer.class);
                    } else {
                        SessionManager.client = snapshot.toObject(Client.class);
                    }
                }
            }
        });
    }

    public static void createClientProfile(Client client, Runnable onDone) {
        FirebaseFirestore.getInstance()
                .collection(clientsCollection)
                .document(client.getUid())
                .set(client)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int x = 0;
                    }
                })
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseAuth
                                .getInstance()
                                .getCurrentUser()
                                .updateProfile(new UserProfileChangeRequest
                                        .Builder()
                                        .setDisplayName("client")
                                        .build()
                                ).addOnCompleteListener(task1 -> {
                                    onDone.run();
                                });
                    }
                });
    }

    public static void createPersonalTrainerProfile(PersonalTrainer personalTrainer, Runnable onDone) {
        FirebaseFirestore.getInstance()
                .collection(personalTrainersCollection)
                .document(personalTrainer.getUid())
                .set(personalTrainer)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseAuth
                                .getInstance()
                                .getCurrentUser()
                                .updateProfile(new UserProfileChangeRequest
                                        .Builder()
                                        .setDisplayName("personalTrainer")
                                        .build()
                                ).addOnCompleteListener(task1 -> {
                                    onDone.run();
                                });
                    }
                });
    }

    public static void createNewPendingRequest(User client, User personalTrainer) {
        FirebaseFirestore.getInstance()
                .collection(personalTrainersCollection)
                .document(personalTrainer.getUid()).
                update("membersShip", personalTrainer.getMembersShip());
        FirebaseFirestore.getInstance()
                .collection(clientsCollection)
                .document(client.getUid())
                .update("membersShip", client.getMembersShip());
    }

    public static void updateMembership(User client, User personalTrainer, MembershipStatus status) {
        FirebaseFirestore.getInstance()
                .collection(personalTrainersCollection)
                .document(personalTrainer.getUid()).
                update("membersShip", personalTrainer.getMembersShip());
        FirebaseFirestore.getInstance()
                .collection(clientsCollection)
                .document(client.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Client myClient = documentSnapshot.toObject(Client.class);
                        for (MemberShip memberShip : myClient.getMembersShip()) {
                            if (memberShip.user.getUid().equals(personalTrainer.getUid())) {
                                memberShip.status = status;
                                break;
                            }
                        }
                        FirebaseFirestore.getInstance()
                                .collection(clientsCollection)
                                .document(client.getUid()).update("membersShip", myClient.getMembersShip());

                    }
                });
    }

    public static void getPersonalTrainerSlots(String personalTrainerId, long date, TrainingsSlotsCallback callback) {
        FirebaseFirestore
                .getInstance()
                .collection(personalTrainersCollection)
                .document(personalTrainerId)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    int dayIndex = Utils.getDayIndexFromMillis(date);
                    if (dayIndex >= 0 && dayIndex <= 5) {
                        PersonalTrainer  personalTrainer = documentSnapshots.toObject(PersonalTrainer.class);
                        DailyOpeningHours openingHours = personalTrainer.getOpeningHours().get(Utils.getDayIndexFromMillis(date));
                        List<Training> dayTraining = personalTrainer.getTrainings().get(Long.toString(date));
                        List<TrainingSlot> slots = new ArrayList<>();

                        for (int i = openingHours.from; i < openingHours.to; i++) {
                            int finalI = i;
                            slots.add(new TrainingSlot(i, dayTraining == null || !dayTraining.stream().anyMatch(a ->
                                    a.getHour() == finalI)));
                        }

                        callback.onDone(slots);
                    } else {
                        callback.onDone(new ArrayList<>());
                    }
                });
    }

    public static void scheduledTraining(PersonalTrainer personalTrainer, long date, int hour, Runnable callback) {
        String dateStr = Long.toString(date);
        if (!personalTrainer.getTrainings().containsKey(dateStr)) {
            personalTrainer.getTrainings().put(dateStr, new ArrayList<>());
        }

        Client client = SessionManager.client;
        Training training = new Training(
                date,
                hour,
                new User(client.getUid(), client.getFullName(), client.getImageUrl()),
                new User(personalTrainer.getUid(), personalTrainer.getFullName(), personalTrainer.getImageUrl())
        );

        personalTrainer.getTrainings().get(dateStr).add(training);
        client.trainings.add(training);

        FirebaseFirestore
                .getInstance()
                .collection(personalTrainersCollection)
                .document(personalTrainer.getUid())
                .update("trainings", personalTrainer.getTrainings())
                .addOnSuccessListener(unused -> {
                    FirebaseFirestore
                            .getInstance()
                            .collection(clientsCollection)
                            .document(client.getUid())
                            .update("trainings", client.trainings)
                            .addOnSuccessListener(result -> {
                                callback.run();
                            })
                            .addOnFailureListener(e -> {
                            });
                })
                .addOnFailureListener(e -> {
                });

    }
}

