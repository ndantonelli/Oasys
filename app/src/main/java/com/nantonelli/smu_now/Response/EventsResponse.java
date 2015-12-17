package com.nantonelli.smu_now.Response;

import com.nantonelli.smu_now.Model.Event;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by ndantonelli on 11/13/15.
 * used to auto parse the JSON returned from the server
 * holds all events for an HTTP Request
 */
@Parcel
public class EventsResponse {
    List<Event> events;

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
