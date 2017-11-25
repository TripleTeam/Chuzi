package com.want.movie.ui;

import android.app.Application;

import com.want.movie.model.data.ApiMovieRepository;
import com.want.movie.model.data.MovieRepository;

public class App extends Application {


    public static MovieRepository getMovieRepository() {
        return ApiMovieRepository.INSTANCE;
    }
}
