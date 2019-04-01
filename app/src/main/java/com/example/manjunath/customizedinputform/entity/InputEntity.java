package com.example.manjunath.customizedinputform.entity;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InputEntity {

    @SerializedName("type")
    private String type;
    @SerializedName("tittle")
    private String tittle;
    @SerializedName("id")
    private String id;
    @SerializedName("dataMap")
    private DataMap dataMap;

    private Bitmap image;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getId() {
        return id;
    }

    public List<String> getOptions() {
        return dataMap.getModels();
    }
}
