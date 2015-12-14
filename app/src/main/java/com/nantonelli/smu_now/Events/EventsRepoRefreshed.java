package com.nantonelli.smu_now.Events;

import com.nantonelli.smu_now.Model.Event;

/**
 * Created by ndantonelli on 11/13/15.
 */
public class EventsRepoRefreshed {
    private int type;
    public EventsRepoRefreshed(int type){
        this.type = type;
    }

    public int getType(){return type;}
}
