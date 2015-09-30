package com.nantonelli.smu_now.Adapters;

import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by ndantonelli on 9/29/15.
 */
public interface OasysRestfulAPI {
    @GET("/group/{id}/users")
    List<String> groupList(@Path("id") int groupId, @QueryMap Map<String, String> options);
}
