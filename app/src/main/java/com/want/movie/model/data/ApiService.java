package com.want.movie.model.data;

import com.want.movie.model.entities.Movie;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiService {
    @GET("smth")
    Single<List<Movie>> getMovies(
            String happiness,
            String bullets,
            String brightness,
            String sexuality
    );
}
