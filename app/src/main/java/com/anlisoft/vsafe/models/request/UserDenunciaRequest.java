package com.anlisoft.vsafe.models.request;


import com.anlisoft.vsafe.models.data.Contribuyente;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDenunciaRequest {

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
    @SerializedName("Denuncia")
    @Expose
    private String denuncia;
    @SerializedName("Motivo")
    @Expose
    private String motivo;
    @SerializedName("Address")
    @Expose
    private String userAddres;

    public UserDenunciaRequest() {
    }

    public UserDenunciaRequest(String operatorId,
                               String latitude,
                               String longitude,
                               Contribuyente contribuyente,
                               String denuncia,
                               String motivo,
                               String userAddres) {

        this.operatorId = operatorId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.contribuyente = contribuyente;
        this.denuncia = denuncia;
        this.motivo = motivo;
        this.userAddres = userAddres;
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

    public String getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(String denuncia) {
        this.denuncia = denuncia;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getUserAddres() {
        return userAddres;
    }

    public void setUserAddres(String userAddres) {
        this.userAddres = userAddres;
    }
}


