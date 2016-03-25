package com.example.android.reviewpopularapp.api;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by chad.yeo on 3/23/2016.
 */
public interface TmdbService {
    @GET("/discover/movie")
    void getMovieList(@Query("sort_by") String sortBy, Callback<MovieResponse> callback);
}
