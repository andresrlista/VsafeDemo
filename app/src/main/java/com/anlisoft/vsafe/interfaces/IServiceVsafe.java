package com.anlisoft.vsafe.interfaces;

import com.anlisoft.vsafe.models.data.Global;
import com.anlisoft.vsafe.models.request.ReporteRequest;
import com.anlisoft.vsafe.models.request.UserDenunciaRequest;
import com.anlisoft.vsafe.models.request.UserEncuestaRequest;
import com.anlisoft.vsafe.models.request.UserRelevamientoRequest;
import com.anlisoft.vsafe.models.response.DenunciaCategoriaResponse;
import com.anlisoft.vsafe.models.response.DenunciaMotivoResponse;
import com.anlisoft.vsafe.models.response.MainReportResponse;
import com.anlisoft.vsafe.models.response.MunicipalidadResponse;
import com.anlisoft.vsafe.models.response.UserDenunciaResponse;
import com.anlisoft.vsafe.models.response.UserEncuestaResponse;
import com.anlisoft.vsafe.models.response.UserLoginResponse;
import com.anlisoft.vsafe.models.response.UserRelevamientoResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IServiceVsafe {

    @POST("login")
    @FormUrlEncoded
    Call<UserLoginResponse> initUserLogin(@Field("Login") String Login,
                                          @Field("Password") String Password);

    @GET(Global.BASE_URL_MUNICIPALIDAD)
    Call<MunicipalidadResponse> getMunicipalidades();

    @POST(Global.BASE_URL_REPORTE)
    Call<MainReportResponse> getReporteMunicipalidad(@Body ReporteRequest reporteRequest);

    @POST("encuesta")
    Call<UserEncuestaResponse> sendUserEncuesta(@HeaderMap Map<String, String> headers,
                                                @Body UserEncuestaRequest userEncuesta);

    @POST("relevamiento")
    Call<UserRelevamientoResponse> sendUserRelevamiento(@HeaderMap Map<String, String> headers,
                                                        @Body UserRelevamientoRequest relevamientoRequest);

    @GET("categoria")
    Call<DenunciaCategoriaResponse> getDenunciaCategoria(@HeaderMap Map<String, String> headers);

    @GET("motivo/{value}")
    Call<DenunciaMotivoResponse> getDenunciaMotivo(@HeaderMap Map<String, String> headers,
                                                   @Path("value") String value);

    @POST("denuncia")
    Call<UserDenunciaResponse> sendUserDenunciaRequest(@HeaderMap Map<String, String> headers,
                                                       @Body UserDenunciaRequest userDenunciaRequest);
}


