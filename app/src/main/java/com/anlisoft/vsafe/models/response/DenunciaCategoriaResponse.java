package com.anlisoft.vsafe.models.response;

import com.anlisoft.vsafe.models.data.DenunciaCategoria;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DenunciaCategoriaResponse {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Result")
    @Expose
    private List<DenunciaCategoria> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DenunciaCategoria> getResult() {
        return result;
    }

    public void setResult(List<DenunciaCategoria> result) {
        this.result = result;
    }

}
