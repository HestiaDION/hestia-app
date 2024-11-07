package com.example.hestia_app.data.api.callbacks;

public interface FirebaseCallback {
    void onSuccess();
    void onFailure(String error);
}