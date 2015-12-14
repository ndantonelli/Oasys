package com.nantonelli.smu_now.Model;

import org.parceler.Parcel;

/**
 * Created by ndantonelli on 12/13/15.
 */
@Parcel
public class DateInfo {
    int day;
    int month;

    public String getDate(){
        return month + "/" + day;
    }
}
