package com.example.android.reviewpopularapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.reviewpopularapp.R;
import com.example.android.reviewpopularapp.api.MovieResponse;
import com.example.android.reviewpopularapp.api.RetrofitAdapter;
import com.example.android.reviewpopularapp.api.TmdbService;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {

    private final static String ARG_ITEMS = "items";
    private final static String ARG_SORT_ORDER = "sort_order";
    private final static String ARG_VIEW_STATE = "view_state";

    private final static int VIEW_STATE_LOADING = 0;
    private final static int VIEW_STATE_ERROR = 1;
    private final static int VIEW_STATE_RESULTS = 2;

    private TextView mErrorTextView;
    private Button mRetryButton;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private String mSortOrder;
    private ListActionListener mActionListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mActionListener = (ListActionListener) activity;
        } catch (ClassCastException e) {
            Log.e(this.getClass().getName(), "Activity must implement " + ListActionListener.class.getName());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean reloadData = (mSortOrder != null && !mSortOrder.equalsIgnoreCase(getSortParam()));
        if (reloadData) {
            loadData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_movies, container, false);
        mErrorTextView = (TextView) v.findViewById(R.id.error_text_view);
        mRetryButton = (Button) v.findViewById(R.id.retry_button);
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retry();
            }
        });
        mProgressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        int orientation = getResources().getConfiguration().orientation;
        int spanCount = (orientation == Configuration.ORIENTATION_LANDSCAPE) ? 4:2;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), spanCount));
        mAdapter = new MovieAdapter(getActivity(), mActionListener);
        mRecyclerView.setAdapter(mAdapter);
        if (savedInstanceState == null) {

        }
    }

    public interface ListActionListener {
    }

    private static
    class MovieAdapter {
    }

    private void showLoadingViews() {
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.GONE);
        mRetryButton.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void showErrorViews() {
        mProgressBar.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.VISIBLE);
        mRetryButton.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void showResultViews() {
        mProgressBar.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.GONE);
        mRetryButton.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void retry() {
        loadData();
    }

    private void loadData() {
        showLoadingViews();
        RestAdapter adapter = RetrofitAdapter.getRestAdapter();
        TmdbService service = adapter.create(TmdbService.class);
        mSortOrder = getSortParam();
    }

    private String getSortParam() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String defaultValue = getString(R.string.sort_order_popularity);
        return pref.getString("sort_order", defaultValue);
    }

    private static class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {
        final private Context mContext;

        private ArrayList<MovieResponse.Movie> mItems;

        final private ListActionListener mActionListener;

        public MovieAdapter(Context context, ListActionListener listener) {
            mContext = context;
            mActionListener = listener;
            mItems = new ArrayList<>();
        }

        @Override
        public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.movie_item, parent, false);
            return new MovieViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MovieViewHolder holder, final int position) {
            Glide.with(mContext)
                    .load(mItems.get(position).getPosterUrl())
                    .centerCrop()
                    .placeholder(R.drawable.movie_placeholder)
                    .crossFade()
                    .into(holder.mPosterView);
            holder.mPosterView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mActionListener.onMovieSelected(mItems.get(position));
                }
            });
        }
    }

    private static class MovieViewHolder extends RecyclerView.ViewHolder {
        final public ImageView mPosterView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            this.mPosterView = (ImageView) itemView.findViewById(R.id.poster_image_view);
        }
    }

    public interface ListActionListener {
        void onMovieSelected(MovieResponse.Movie movie);
    }
}
