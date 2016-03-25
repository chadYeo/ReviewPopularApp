package com.example.android.reviewpopularapp.api;

import com.example.android.reviewpopularapp.BuildConfig;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by chad.yeo on 3/22/2016.
 */
public class RetrofitAdapter {
    public final static String BASE_URL = "http://api.themoviedb.org/3/";
    public final static String API_KEY = "6d719c665708dd893bf43660551867a8";
    private static RestAdapter mRestAdapter;

    public static RestAdapter getRestAdapter() {
        if (mRestAdapter == null) {
            mRestAdapter = new RestAdapter.Builder()
                    .setEndpoint(BASE_URL)
                    .setRequestInterceptor(mRequestInterceptor)
                    .build();
            if (BuildConfig.DEBUG) {
                mRestAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
            }
        }
        return mRestAdapter;
    }

    public static RequestInterceptor mRequestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {
            request.addQueryParam("api_key", API_KEY);
        }
    };
}
