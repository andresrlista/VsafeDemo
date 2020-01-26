package com.anlisoft.vsafe.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Reporte {
    @SerializedName("TotalRecibidas")
    @Expose
    public Integer totalRecibidas;
    @SerializedName("TotalFinalizadas")
    @Expose
    public Integer totalFinalizadas;
    @SerializedName("TotalNoche")
    @Expose
    public Integer totalNoche;
    @SerializedName("TotalDia")
    @Expose
    public Integer totalDia;
    @SerializedName("TotalTarde")
    @Expose
    public Integer totalTarde;
    @SerializedName("TotalDenunciaPerfil")
    @Expose
    public List<TotalDenunciaPerfil> totalDenunciaPerfil;
    @SerializedName("TotalDenunciaOrigen")
    @Expose
    public List<TotalDenunciaOrigen> totalDenunciaOrigen;
    @SerializedName("Perfiles")
    @Expose
    public List<Perfiles> perfilesList;


}

