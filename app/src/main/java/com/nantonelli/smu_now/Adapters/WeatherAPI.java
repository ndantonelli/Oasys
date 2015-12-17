package com.nantonelli.smu_now.Adapters;

import com.nantonelli.smu_now.Model.WeatherResponse;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by ndantonelli on 12/13/15.
 * interface for getting the weather for university park
 */
public interface WeatherAPI {
    @GET("/75205.json")
    public void getWeather(Callback<WeatherResponse> response);
}
