package com.want.movie.model.entities;

public class Movie {
    private String title;
    private String cover;
    private String url;

    public Movie(String title, String cover, String url) {
        this.title = title;
        this.cover = cover;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getCover() {
        return cover;
    }

    public String getUrl() {
        return url;
    }
}
