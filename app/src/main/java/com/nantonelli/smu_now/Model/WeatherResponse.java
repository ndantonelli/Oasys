package com.nantonelli.smu_now.Model;

import org.parceler.Parcel;

/**
 * Created by ndantonelli on 12/13/15.
 * used while parsing the weather underground json response into objects
 */
@Parcel
public class WeatherResponse {
    Forecast forecast;

    public Forecast getForecast(){
        return forecast;
    }
}
