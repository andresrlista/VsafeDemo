package com.anlisoft.vsafe.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDenunciaResponse {

    @SerializedName("Message")
    @Expose
    private String Message;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    @Override
    public String toString() {
        return this.Message;
    }

}
