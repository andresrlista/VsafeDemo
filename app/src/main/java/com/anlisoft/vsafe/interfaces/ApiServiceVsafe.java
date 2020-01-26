package com.anlisoft.vsafe.interfaces;

import com.anlisoft.vsafe.utils.RetrofitClient;

public class ApiServiceVsafe {

    private ApiServiceVsafe() {
    }

    public static IServiceVsafe getAPIService(String baseUrl) {
        return RetrofitClient
                .getClient(baseUrl)
                .create(IServiceVsafe.class);
    }
}

