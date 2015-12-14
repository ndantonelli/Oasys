package com.nantonelli.smu_now.Adapters;



import com.nantonelli.smu_now.Model.Comment;
import com.nantonelli.smu_now.Model.Event;
import com.nantonelli.smu_now.Response.EventsResponse;
import com.nantonelli.smu_now.Response.UserResponse;

import java.util.List;
import java.util.Map;


import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/**
 * Created by ndantonelli on 9/29/15.
 */
public interface OasysRestfulAPI {
    @GET("/events")
    public void getEvents(@Query("time_start_buffer") String buff, Callback<List<Event>> response);

    @GET("/comments")
    public void getComments(@Query("event_id") int id, Callback<List<Comment>> response);

    @GET("/events")
    public void getPopularEvents(@Query("time_start_buffer") String buff, @Query("popular") String popular, Callback<List<Event>> response);

    @GET("/events")
    public void getMyEvents(@Query("time_start_buffer") String buff, @Query("user_id") int ID, Callback<List<Event>> response);

    @FormUrlEncoded
    @POST("/user")
    public void createUser(@Field("advertising_id") String adId, @Field("user_name") String username, Callback<UserResponse> response);

    @FormUrlEncoded
    @POST("/attendance")
    public void postAttending(@Field("user_id") int uid, @Field("event_id") int id, Callback<Response> responseCallback);

    @FormUrlEncoded
    @POST("/attendance")
    public void removeAttending(@Field("user_id") int uid, @Field("event_id") int id, @Field("status") String status, Callback<Response> responseCallback);

    @FormUrlEncoded
    @POST("/comments")
    public void postComment(@Field("user_id") int uid, @Field("event_id") int eid, @Field("comment") String comment, Callback<Response> response);
}
