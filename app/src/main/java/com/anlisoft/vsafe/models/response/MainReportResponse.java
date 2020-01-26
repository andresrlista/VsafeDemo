package com.anlisoft.vsafe.models.response;

import com.anlisoft.vsafe.models.data.Reporte;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MainReportResponse {

            @SerializedName("success")
            @Expose
            public Boolean success;
            @SerializedName("codigo")
            @Expose
            public Integer codigo;
            @SerializedName("message")
            @Expose
            public String message;
            @SerializedName("resultado")
            @Expose
            public List<Reporte> reportResult;
}
