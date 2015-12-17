package com.nantonelli.smu_now.Events;

/**
 * Created by ndantonelli on 9/29/15.
 * event used by the otto event bus to tell fragments that the back button has been pressed
 */
public class BackEvent {
    private int pos;
    public BackEvent(int pos){this.pos = pos;}
    public int getPos(){return pos;}
}
