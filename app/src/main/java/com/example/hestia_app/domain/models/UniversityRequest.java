package com.example.hestia_app.domain.models;

import java.util.UUID;

public class UniversityRequest {
    private UUID university_uuid;

    public UniversityRequest(UUID university_uuid) {
        this.university_uuid = university_uuid;
    }

    public UUID getUniversityUuid() {
        return university_uuid;
    }

    public void setUniversityUuid(UUID university_uuid) {
        this.university_uuid = university_uuid;
    }
}
