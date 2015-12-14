package com.nantonelli.smu_now.Model;

import org.parceler.Parcel;

/**
 * Created by ndantonelli on 12/13/15.
 */
@Parcel
public class DailyWeather {
    DateInfo date;
    HighTemp high;
    LowTemp low;
    String icon_url;

    public DateInfo getDate(){
        return date;
    }

    public HighTemp getHighTemp(){
        return high;
    }

    public LowTemp getLow(){
        return low;
    }

    public String getIcon_url(){
        return icon_url;
    }
}
