package com.anlisoft.vsafe.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalDenunciaPerfil implements Comparable<TotalDenunciaPerfil>{

    @SerializedName("Id")
    @Expose
    public Integer idPerfil;
    @SerializedName("Name")
    @Expose
    public String name;
    @SerializedName("TotalEmergenciaPerfil")
    @Expose
    public Integer totalEmergenciaPerfil;
    @SerializedName("TotalSegundos")
    @Expose
    public int totalSegundos;

    public Integer getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Integer idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalEmergenciaPerfil() {
        return totalEmergenciaPerfil;
    }

    public void setTotalEmergenciaPerfil(Integer totalEmergenciaPerfil) {
        this.totalEmergenciaPerfil = totalEmergenciaPerfil;
    }

    public Integer getTotalSegundos() {
        return totalSegundos;
    }

    public void setTotalSegundos(Integer totalSegundos) {
        this.totalSegundos = totalSegundos;
    }

    @Override
    public int compareTo(TotalDenunciaPerfil o) {
        return this.getTotalSegundos().compareTo(o.getTotalSegundos());
    }
}
