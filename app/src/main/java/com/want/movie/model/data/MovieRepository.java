package com.want.movie.model.data;

import com.want.movie.model.entities.Filter;
import com.want.movie.model.entities.Movie;

import java.util.List;

import io.reactivex.Single;

public interface MovieRepository {
    Single<List<Movie>> getMovies(Filter filter);
}
