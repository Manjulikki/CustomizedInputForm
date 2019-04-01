package com.example.manjunath.customizedinputform.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class DataMap {

    @SerializedName("models")
    private List<String> models;

    public List<String> getModels() {
        return models;
    }
}
