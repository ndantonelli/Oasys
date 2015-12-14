package com.nantonelli.smu_now.Model;

import org.parceler.Parcel;

/**
 * Created by ndantonelli on 12/13/15.
 */
@Parcel
public class LowTemp {
    String fahrenheit;

    public String getFahrenheit(){
        return fahrenheit + "\u00B0F";
    }
}
