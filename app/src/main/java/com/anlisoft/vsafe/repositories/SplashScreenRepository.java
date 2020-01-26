package com.anlisoft.vsafe.repositories;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.anlisoft.vsafe.interfaces.IServiceVsafe;
import com.anlisoft.vsafe.models.data.Municipalidad;
import com.anlisoft.vsafe.models.response.MunicipalidadResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.anlisoft.vsafe.models.data.Global.KEY_CURRENT_MUNICIPALIDAD;
import static com.anlisoft.vsafe.models.data.Global.KEY_USERLOGGED;
import static com.anlisoft.vsafe.models.data.Global.KEY_VSAFE;
import static com.anlisoft.vsafe.models.data.Global.gMunicipalidadesListado;
import static com.anlisoft.vsafe.models.data.Global.gRetrofit;
import static com.anlisoft.vsafe.models.data.Global.gSessionManager;
import static com.anlisoft.vsafe.models.data.Global.gVsafeWebServices;


/**
 * Singleton pattern
 */
public class SplashScreenRepository {


    private static SplashScreenRepository instance = null;
    private MutableLiveData<MunicipalidadResponse> dataResponseMunicipalidades = new MutableLiveData<>();

    public static SplashScreenRepository getInstance() {

        if (instance == null) {
            instance = new SplashScreenRepository();

        }
        return instance;
    }

    public MutableLiveData<MunicipalidadResponse> getDataResponseMunicipalidades() {
        gVsafeWebServices.getMunicipalidades().enqueue(new Callback<MunicipalidadResponse>() {
            @Override
            public void onResponse(@NonNull Call<MunicipalidadResponse> call,
                                   @NonNull Response<MunicipalidadResponse> response) {

                dataResponseMunicipalidades.setValue(response.body());

            }

            @Override
            public void onFailure(@NonNull Call<MunicipalidadResponse> call,
                                  @NonNull Throwable t) {
                dataResponseMunicipalidades.setValue(null);
            }
        });

        return dataResponseMunicipalidades;
    }

    public void setAllMunicipalidades(List<Municipalidad> municipalidades) {
        Municipalidad municipalidadDefault = new Municipalidad();
        gMunicipalidadesListado.clear();
        municipalidadDefault.setDescription("Seleccione una Municipalidad:");
        gMunicipalidadesListado.add(0, municipalidadDefault);
        gMunicipalidadesListado.addAll(municipalidades);
    }

    public void setWebServiceApiSource(String urlBase, Class<IServiceVsafe> classService) {
        gRetrofit = null;
        gVsafeWebServices = null;
        gRetrofit = new Retrofit.Builder()
                .baseUrl(urlBase)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gVsafeWebServices = gRetrofit.create(classService);
    }

    public void initSessionManager(Context context) {
        gSessionManager = context.getSharedPreferences(KEY_VSAFE, Context.MODE_PRIVATE);
    }

    public String getCurrentUrlBase(Context context) {
        String currentUrlBase;
        gSessionManager = context.getSharedPreferences(KEY_VSAFE, Context.MODE_PRIVATE);
        currentUrlBase = gSessionManager.getString(KEY_CURRENT_MUNICIPALIDAD, "-");
        return currentUrlBase;
    }

    public boolean isUserLogged() {

        return gSessionManager.getBoolean(KEY_USERLOGGED, false);
    }

}



