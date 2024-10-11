package com.example.hestia_app.data.api.callbacks;

import java.util.Map;

public interface ApiServiceCallback {
    void onApiServiceSuccess(Map<String, String> message);
    void onApiServiceFailure(String errorMessage);
}
