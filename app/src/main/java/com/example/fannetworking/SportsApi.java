package com.example.fannetworking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SportsApi {
    @GET("search_all_teams.php?l=English%20Premier%20League")
    Call<TeamsResponse> getAllTeams(@Query("l") String league);
}