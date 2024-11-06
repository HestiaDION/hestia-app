package com.example.hestia_app.domain.models;

import java.util.HashMap;
import java.util.List;

public class RecomendacoesMoradia {
    private List<HashMap<String, String>> houses;
    private String message;

    public List<HashMap<String, String>> getHouses() {
        return houses;
    }

    public RecomendacoesMoradia(List<HashMap<String, String>> houses, String message) {
        this.houses = houses;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void setHouses(List<HashMap<String, String>> houses) {
        this.houses = houses;
    }
}
