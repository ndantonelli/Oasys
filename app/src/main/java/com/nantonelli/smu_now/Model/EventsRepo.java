package com.nantonelli.smu_now.Model;

import com.nantonelli.smu_now.Adapters.OasysRestfulAPI;
import com.nantonelli.smu_now.Events.EventsRepoRefreshed;
import com.nantonelli.smu_now.OasysApplication;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by ndantonelli on 11/13/15.
 */
public class EventsRepo {

    @Inject Bus eventBus;

    private List<Event> timeEvents;
    private List<Event> popularEvents;
    private List<Event> myEvents;

    public EventsRepo(){
        timeEvents = new ArrayList<>();
        popularEvents = new ArrayList<>();
        myEvents = new ArrayList<>();
        OasysApplication.getInstance().getObjectGraph().inject(this);
    }

    public void setTimeEvents(List<Event> events){
        this.timeEvents = events;
        eventBus.post(new EventsRepoRefreshed(0));
    }

    public List<Event> getTimeEvents(){
        return timeEvents;
    }

    public void setPopularEvents(List<Event> events){
        this.popularEvents = events;
        eventBus.post(new EventsRepoRefreshed(1));
    }

    public List<Event> getPopularEvents(){
        return popularEvents;
    }

    public void setMyEvents(List<Event> events){
        this.myEvents = events;
        eventBus.post(new EventsRepoRefreshed(2));
    }

    public void updateAttending(Event event, boolean adding){
        for(int i = 0; i < timeEvents.size(); i++)
            if(timeEvents.get(i).getEvent_id() == event.getEvent_id())
                timeEvents.get(i).setAttendance(event.getAttendance());
        for(int i = 0; i < popularEvents.size(); i++)
            if(popularEvents.get(i).getEvent_id() == event.getEvent_id())
                popularEvents.get(i).setAttendance(event.getAttendance());
        if(adding)
            myEvents.add(event);
        else{
            for(int i = 0; i < myEvents.size(); i++)
                if (myEvents.get(i).getEvent_id() == event.getEvent_id())
                    myEvents.remove(i);
        }
        eventBus.post(new EventsRepoRefreshed(2));
    }


    public List<Event> getMyEvents(){
        return myEvents;
    }

    public boolean checkAttending(int id){
        for(int i = 0; i < myEvents.size(); i++)
            if(myEvents.get(i).getEvent_id() == id)
                return true;
        return false;
    }
}
