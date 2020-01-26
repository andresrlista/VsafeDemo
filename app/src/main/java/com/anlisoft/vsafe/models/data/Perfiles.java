package com.anlisoft.vsafe.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Perfiles {

    @SerializedName("Name")
    @Expose
    public String name;
    @SerializedName("Id")
    @Expose
    public Integer idPerfil;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Integer idPerfil) {
        this.idPerfil = idPerfil;
    }
}
