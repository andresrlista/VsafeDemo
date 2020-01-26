package com.anlisoft.vsafe.models.data;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DenunciaCategoria {
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("GrupoName")
    @Expose
    private String grupoName;

    @NonNull
    @Override
    public String toString(){
        return this.grupoName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGrupoName() {
        return grupoName;
    }

    public void setGrupoName(String grupoName) {
        this.grupoName = grupoName;
    }

}
