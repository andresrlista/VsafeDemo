package com.anlisoft.vsafe.models.response;

import com.anlisoft.vsafe.models.data.Motivo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DenunciaMotivoResponse {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Result")
    @Expose
    private List<Motivo> Result = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Motivo> getMotivoResult() {
        return Result;
    }

    public void setMotivoResult(List<Motivo> motivoResult) {
        this.Result = motivoResult;
    }



}
