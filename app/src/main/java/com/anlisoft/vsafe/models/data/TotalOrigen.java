package com.anlisoft.vsafe.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class TotalOrigen {

        @SerializedName("Name")
        @Expose
        public String name;
        @SerializedName("TotalEmergenciaOrigen")
        @Expose
        public Integer totalEmergenciaOrigen;
}
