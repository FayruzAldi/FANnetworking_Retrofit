package com.example.fannetworking;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SportsApi sportsApi;
    private RecyclerView teamRecyclerView;
    private TeamAdapter teamAdapter;
    private ArrayList<Team> teamList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        teamRecyclerView = findViewById(R.id.teamRecyclerView);
        teamAdapter = new TeamAdapter(teamList);
        teamRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        teamRecyclerView.setAdapter(teamAdapter);

        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.thesportsdb.com/api/v1/json/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create API interface
        sportsApi = retrofit.create(SportsApi.class);

        // Call API method
        getAllTeams("English Premier League");
    }

    private void getAllTeams(String league) {
        Call<TeamsResponse> call = sportsApi.getAllTeams(league);
        call.enqueue(new Callback<TeamsResponse>() {
            @Override
            public void onResponse(Call<TeamsResponse> call, Response<TeamsResponse> response) {
                if (response.isSuccessful()) {
                    TeamsResponse teamsResponse = response.body();
                    if (teamsResponse != null && teamsResponse.getTeams() != null) {
                        teamList.addAll(teamsResponse.getTeams());
                        teamAdapter.notifyDataSetChanged(); // Notify adapter about data change
                    } else {
                        Log.e(TAG, "Empty response or null data");
                    }
                } else {
                    Log.e(TAG, "Unsuccessful response: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TeamsResponse> call, Throwable t) {
                // Handle network failure
                Log.e(TAG, "Network failure", t);
            }
        });
    }
}
