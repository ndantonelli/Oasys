package com.nantonelli.smu_now.Events;

import com.nantonelli.smu_now.Model.Event;

/**
 * Created by ndantonelli on 11/13/15.
 * the event repo has been refreshed with new time, popular, or my events
 * 0 = time, 1 = popular, 2 = my events
 */
public class EventsRepoRefreshed {
    private int type;
    public EventsRepoRefreshed(int type){
        this.type = type;
    }

    public int getType(){return type;}
}
