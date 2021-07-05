package com.gads.leaderboad.Services;


import com.gads.leaderboad.Model.LeaderIQ;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LeaderIQFetchService {
    @GET("/api/skilliq")
    Call<List<LeaderIQ>> getIQ();
}
