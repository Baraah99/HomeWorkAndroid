package com.example.charhomework;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitAPI {

    @GET("/getNewKids/{period}")
    Call<HashMap<String,Integer>>getNewKids(@Path("period") int period);

    @GET("/getNewParents/{period}")
    Call<HashMap<String,Integer>> getNewParents(@Path("period") int period);

    @GET("/getKidsCountByCategory/{period}")
    Call<HashMap<String,Integer>> getKidsCountByCategory(@Path("period") int period);

    @GET("/getActivityTime/{period}")
    Call<HashMap<String,Double>> getActivityTime(@Path("period") int period);

}



