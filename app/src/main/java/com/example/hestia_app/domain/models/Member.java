package com.example.hestia_app.domain.models;

public class Member {
    private String name;
    private String gender;
    private String age;
    private int profileImageResId;

    public Member(String name, String gender, String age, int profileImageResId) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.profileImageResId = profileImageResId;
    }

    public Member(String name, String gender, String age) {
        this.name = name;
        this.gender = gender;
        this.age = age;
    }


    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getAge() {
        return age;
    }

    public int getProfileImageResId() {
        return profileImageResId;
    }
}
