package com.anlisoft.vsafe.models.request;

import com.anlisoft.vsafe.models.data.Relevamiento;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserRelevamientoRequest {

    @SerializedName("OperatorId")
    @Expose
    private String operatorId;
    @SerializedName("Latitude")
    @Expose
    private String latitude;
    @SerializedName("Longitude")
    @Expose
    private String longitude;
    @SerializedName("Relevamiento")
    @Expose
    private List<Relevamiento> relevamiento;

    public UserRelevamientoRequest(String operatorId, String latitude, String longitude, List<Relevamiento> relevamiento) {
        this.operatorId = operatorId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.relevamiento = relevamiento;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public List<Relevamiento> getRelevamiento() {
        return relevamiento;
    }

    public void setRelevamiento(List<Relevamiento> relevamiento) {
        this.relevamiento = relevamiento;
    }
}
