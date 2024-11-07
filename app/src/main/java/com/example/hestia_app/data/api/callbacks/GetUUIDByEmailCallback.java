package com.example.hestia_app.data.api.callbacks;

public interface GetUUIDByEmailCallback {
    void onGetUUIDByEmailSuccess(String uuid);
    void onGetUUIDByEmailFailure(String erroMessage);
}
