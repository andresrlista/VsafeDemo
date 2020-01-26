package com.anlisoft.vsafe.models.request;

import com.anlisoft.vsafe.models.data.Contribuyente;
import com.anlisoft.vsafe.models.data.Relevamiento;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserEncuestaRequest {
    @SerializedName("OperatorId")
    @Expose
    private String operatorId;
    @SerializedName("Latitude")
    @Expose
    private String latitude;
    @SerializedName("Longitude")
    @Expose
    private String longitude;
    @SerializedName("Contribuyente")
    @Expose
    private Contribuyente contribuyente;
    @SerializedName("Relevamiento")
    @Expose
    private List<Relevamiento> relevamiento;

    public UserEncuestaRequest() {
    }

    public UserEncuestaRequest(String operatorId,
                               String latitude,
                               String longitude,
                               Contribuyente contribuyente,
                               List<Relevamiento> relevamiento) {
        this.operatorId = operatorId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.contribuyente = contribuyente;
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

    public Contribuyente getContribuyente() {
        return contribuyente;
    }

    public void setContribuyente(Contribuyente contribuyente) {
        this.contribuyente = contribuyente;
    }

    public List<Relevamiento> getRelevamiento() {
        return relevamiento;
    }

    public void setRelevamiento(List<Relevamiento> relevamiento) {
        this.relevamiento = relevamiento;
    }
}
