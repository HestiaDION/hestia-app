package com.example.hestia_app.data.api.callbacks;

import com.example.hestia_app.domain.models.UniversitarioMoradia;

public interface JoinMoradiaCallback {
    void onJoinSucess(UniversitarioMoradia universitarioMoradia, boolean isJoined);
    void onJoinFailure(Throwable throwable);
}
