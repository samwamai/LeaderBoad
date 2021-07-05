package com.gads.leaderboad.Services;


import com.gads.leaderboad.Model.LeaderHours;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LeaderHoursFetchService {
    @GET("/api/hours")
    Call<List<LeaderHours>> getHours();
}
