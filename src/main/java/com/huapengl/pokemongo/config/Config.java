package com.huapengl.pokemongo.config;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Config {

    private final List<String> cities;
    private final List<String> googleApiKeys;

    public Config(@JsonProperty("cities") List<String> cities,
            @JsonProperty("google_api_keys") List<String> googleApiKeys) {
        this.cities = cities;
        this.googleApiKeys = googleApiKeys;
    }

    public List<String> getCities() {
        return cities;
    }

    public List<String> getGoogleApiKeys() {
        return googleApiKeys;
    }
}
