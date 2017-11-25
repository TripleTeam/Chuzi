package com.want.movie.ui;

import android.app.Application;

import com.want.movie.model.data.MovieRepository;
import com.want.movie.model.data.MovieRepositoryMock;

public class App extends Application {


    public static MovieRepository getMovieRepository() {
        return MovieRepositoryMock.INSTANCE;
    }
}
