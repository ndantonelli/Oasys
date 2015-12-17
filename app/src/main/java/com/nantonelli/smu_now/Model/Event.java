package com.nantonelli.smu_now.Model;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by ndantonelli on 11/13/15.
 * building block of the whole app, everything relies upon the events
 */
@Parcel
public class Event {
    int event_id;
    String event_name;
    String host_name;
    String date_start;
    String time_start;
    String address;
    String description;
    String photo_url;
    int attendance;

    public String getPhoto_url(){return photo_url;}

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime_start() {

        String[] timeSplit = time_start.split(":");
        int time1 = Integer.parseInt(timeSplit[0]);
        String end = " AM";
        if(time1 == 0)
            time1 = 12;
        if(Integer.parseInt(timeSplit[0]) > 12) {
            time1 -= 12;
            end = " PM";
        }

        return time1 + ":" + timeSplit[1] + end;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getDate_start() {
        String[] startSplit = date_start.split("-");

        return startSplit[1] + "/" + startSplit[2] + "/" + startSplit[0];
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getHost_name() {

        return host_name;
    }

    public void setHost_name(String host_name) {
        this.host_name = host_name;
    }

    public String getEvent_name() {

        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public int getEvent_id() {

        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }


}
