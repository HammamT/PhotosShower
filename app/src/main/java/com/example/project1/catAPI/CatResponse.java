package com.example.project1.catAPI;

import com.google.gson.annotations.SerializedName;

public class CatResponse {

    @SerializedName("url")
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
