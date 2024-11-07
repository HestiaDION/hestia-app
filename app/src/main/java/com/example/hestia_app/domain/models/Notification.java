package com.example.hestia_app.domain.models;

public class Notification {
    private String title;
    private String date;
    private String description;

    public Notification(String title, String date, String description) {
        this.title = title;
        this.date = date;
        this.description = description;
    }

    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getDescription() { return description; }
}
