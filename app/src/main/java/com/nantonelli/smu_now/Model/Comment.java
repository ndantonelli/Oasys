package com.nantonelli.smu_now.Model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by ndantonelli on 12/10/15.
 * used to parse comment json objects
 */
@Parcel
public class Comment {
    @SerializedName("comment_id") int id;
    int user_id;
    String comment;
    String timestamp;
    String username;

    public String getName(){return username;}
    @Override
    public String toString(){
        return comment;
    }

    @ParcelConstructor
    public Comment(){}
    public Comment(String com, String user){
        comment = com;
        username = user;
    }
}
