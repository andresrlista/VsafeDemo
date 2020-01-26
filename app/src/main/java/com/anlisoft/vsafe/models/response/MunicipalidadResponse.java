package com.anlisoft.vsafe.models.response;

import com.anlisoft.vsafe.models.data.Municipalidad;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MunicipalidadResponse {

        @SerializedName("Success")
        @Expose
        private Boolean success;
        @SerializedName("Cant")
        @Expose
        private Integer cant;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("Munic")
        @Expose
        private List<Municipalidad> munic = null;

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public Integer getCant() {
            return cant;
        }

        public void setCant(Integer cant) {
            this.cant = cant;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<Municipalidad> getMunicipalidad() {
            return munic;
        }

        public void setMunic(List<Municipalidad> munic) {
            this.munic = munic;
        }

}
