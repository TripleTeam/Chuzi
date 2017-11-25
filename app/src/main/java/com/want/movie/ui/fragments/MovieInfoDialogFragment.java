package com.want.movie.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.want.movie.model.entities.Movie;

/**
 * Created by Slavik on 25-Nov-17.
 * Yippie-Kay-Yay!
 */

public class MovieInfoDialogFragment extends DialogFragment {

    private static final String TAG = "movie";

    public MovieInfoDialogFragment() {
    }

    public static MovieInfoDialogFragment instance(Movie movie) {
        MovieInfoDialogFragment fragment = new MovieInfoDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(MovieInfoDialogFragment.TAG, movie);
        fragment.setArguments(args);
        return fragment;
    }

}
