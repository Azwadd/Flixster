package com.codepath.azwad.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.azwad.flixster.R;
import com.codepath.azwad.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class DetailedActivity extends YouTubeBaseActivity {

    private static final String YOUTUBE_API_KEY = "AIzaSyASRXkB0Rx1fA5YQ52csk8bPzZixFYSVg0";
    private static final String VIDEO_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    TextView tvTitles;
    TextView tvOverviews;
    RatingBar ratingbar;
    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        tvTitles = findViewById(R.id.tvTitles);
        tvOverviews = findViewById(R.id.tvOverviews);
        ratingbar = findViewById(R.id.ratingBar);
        youTubePlayerView = findViewById(R.id.player);

        String title = getIntent().getStringExtra("title");
        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        tvTitles.setText(movie.getTitle());
        tvOverviews.setText(movie.getOverview());
        ratingbar.setRating((float) movie.getRating());

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEO_URL, movie.getMoveID()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d("DetailedActivity", "onSuccess");
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if (results.length() == 0) { return; }
                    String youtubeKey = results.getJSONObject(0).getString("key");
                    Log.d("DetailedActivity", youtubeKey);
                    initializeYoutube(youtubeKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d("DetailedActivity", "onFailure");
            }
        });
    }

    void initializeYoutube(final String youtubekey) {
        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("DetailedActivity", "onInitializationSuccess");
                youTubePlayer.cueVideo(youtubekey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("DetailedActivity", "onInitializationFailure");
            }
        });
    }
}

