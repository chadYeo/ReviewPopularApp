package com.example.android.reviewpopularapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.android.reviewpopularapp.api.MovieResponse;
import com.example.android.reviewpopularapp.fragments.MovieDetailFragment;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        MovieResponse.Movie movie;
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            movie = extra.getParcelable(MovieDetailFragment.ARG_MOVIE);
        } else {
            throw new NullPointerException("No movie found in intent extra");
        }
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(movie.title);
        }

        MovieDetailFragment fragment = MovieDetailFragment.getInstance(movie);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
