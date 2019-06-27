package com.codepath.debuggingchallenges.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.codepath.debuggingchallenges.R;
import com.codepath.debuggingchallenges.adapters.MoviesAdapter;
import com.codepath.debuggingchallenges.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MoviesActivity extends AppCompatActivity {

    private static final String API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";

    RecyclerView rvMovies;
    MoviesAdapter adapter;
    ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        rvMovies = findViewById(R.id.rvMovies);

        //init movies
        movies = new ArrayList<>();

        // Create the adapter to convert the array to views
        // MoviesAdapter adapter = new MoviesAdapter(movies);
        adapter = new MoviesAdapter(movies);

        // Attach the adapter to a ListView
        rvMovies.setAdapter(adapter);

        // set layout manager
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        fetchMovies();
    }


    private void fetchMovies() {
        Toast.makeText(this, "fetchMovies called!", Toast.LENGTH_LONG).show();
        String url = " https://api.themoviedb.org/3/movie/now_playing?api_key=";
        AsyncHttpClient client = new AsyncHttpClient();

        // create params
        RequestParams params = new RequestParams();

        params.put("api_key", API_KEY); //API KEY is always required

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("MSG", "successful HttpRequest!");
                try {
                    JSONArray moviesJson = response.getJSONArray("results");
                    movies.addAll(Movie.fromJSONArray(moviesJson));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("EMSG", "on failure ran");
            }
        });
    }
}
