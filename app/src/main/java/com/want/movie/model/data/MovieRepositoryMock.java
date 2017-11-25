package com.want.movie.model.data;

import com.want.movie.model.entities.Filter;
import com.want.movie.model.entities.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;

public enum MovieRepositoryMock implements MovieRepository {
    INSTANCE;

    @Override
    public Single<List<Movie>> getMovies(Filter filter) {
        return Single.fromCallable(new Callable<List<Movie>>() {
            @Override
            public List<Movie> call() throws Exception {
                return mockMovies();
            }
        });
    }

    private List<Movie> mockMovies() {
        int count = 10;
        List<Movie> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(generateMovie(i));
        }
        return result;
    }

    private Movie generateMovie(int position) {
        String cover = "https://images-na.ssl-images-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_UX182_CR0,0,182,268_AL_.jpg";
        String url = "http://www.imdb.com/title/tt0468569/";
        return new Movie("Title " + position + " title of the film will be here", cover, url);
    }
}
