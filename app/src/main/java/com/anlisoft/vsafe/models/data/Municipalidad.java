package com.anlisoft.vsafe.models.data;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Municipalidad {

    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("baseurl")
    @Expose
    private String baseurl;
    @SerializedName("port")
    @Expose
    private Integer port;
    @SerializedName("api")
    @Expose
    private String api;
    @SerializedName("getManagerial")
    @Expose
    private String getManagerial;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getGetManagerial() {
        return getManagerial;
    }

    public void setGetManagerial(String getManagerial) {
        this.getManagerial = getManagerial;
    }

    @NonNull
    @Override
    public String toString() {
        return description;
    }
}
