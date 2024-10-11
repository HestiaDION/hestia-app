package com.example.hestia_app.utils;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FirestoreUtil {

    public static boolean registerUser(String id, HashMap<String, String> user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("users").document(id).set(user).isSuccessful();
    }

    public static boolean updateUser(String id, HashMap<String, Object> user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("users").document(id).update(user).isSuccessful();
    }

    public static HashMap<String, Object> getUser(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return (HashMap<String, Object>) db.collection("users").document(id).get().getResult().getData();
    }
}
