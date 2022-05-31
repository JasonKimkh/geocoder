package com.example.geocoder;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {
    @GET("1360000/VilageFcstInfoService_2.0/getUltraSrtNcst")
    Call<WeatherResult> getWeather(
            @Query("serviceKey") String serviceKey,
            @Query("numOfRows") String numOfRows,
            @Query("pageNo") String pageNo,
            @Query("dataType") String dataType,
            @Query("base_date") String base_date,
            @Query("base_time") String base_time,
            @Query("nx") String nx,
            @Query("ny") String ny);
}
