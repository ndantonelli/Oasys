package com.nantonelli.smu_now.Response;

import org.parceler.Parcel;

/**
 * Created by ndantonelli on 12/12/15.
 * Used to parse user create requests into an object
 */
@Parcel
public class UserResponse {
    int user_id;
    public int getUser_id(){return user_id;}
}
