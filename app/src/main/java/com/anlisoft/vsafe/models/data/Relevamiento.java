package com.anlisoft.vsafe.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Relevamiento {
    @SerializedName("Pregunta")
    @Expose
    private String pregunta;
    @SerializedName("Respuesta")
    @Expose
    private String respuesta;

    public Relevamiento() {
    }

    public Relevamiento(String pregunta, String respuesta) {
        this.pregunta = pregunta;
        this.respuesta = respuesta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
