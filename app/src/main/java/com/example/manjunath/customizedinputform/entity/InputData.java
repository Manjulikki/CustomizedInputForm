package com.example.manjunath.customizedinputform.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InputData {
    @SerializedName("input")
    ArrayList<ArrayList<InputEntity>> inputEntities;

    public ArrayList<ArrayList<InputEntity>> getInputEntities() {
        return inputEntities;
    }
}
