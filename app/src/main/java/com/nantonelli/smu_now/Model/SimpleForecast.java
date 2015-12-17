package com.nantonelli.smu_now.Model;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by ndantonelli on 12/13/15.
 * used while parsing the weather underground json response into objects
 */
@Parcel
public class SimpleForecast {
    List<DailyWeather> forecastday;

    public List<DailyWeather> getDailyForecast(){
        return forecastday;
    }
}
