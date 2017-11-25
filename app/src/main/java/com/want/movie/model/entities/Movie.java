package com.want.movie.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String title;
    private String cover;
    private String url;
    private String year;
    private String genre;
    private String description;

    public Movie(String title, String cover, String url, String year, String genre, String description) {
        this.title = title;
        this.cover = cover;
        this.url = url;
        this.year = year;
        this.genre = genre;
        this.description = description;
    }

    protected Movie(Parcel in) {
        title = in.readString();
        cover = in.readString();
        url = in.readString();
        year = in.readString();
        genre = in.readString();
        description = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(cover);
        dest.writeString(url);
        dest.writeString(year);
        dest.writeString(genre);
        dest.writeString(description);
    }

    public String getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (title != null ? !title.equals(movie.title) : movie.title != null) return false;
        if (cover != null ? !cover.equals(movie.cover) : movie.cover != null) return false;
        if (url != null ? !url.equals(movie.url) : movie.url != null) return false;
        if (year != null ? !year.equals(movie.year) : movie.year != null) return false;
        if (genre != null ? !genre.equals(movie.genre) : movie.genre != null) return false;
        return description != null ? description.equals(movie.description) : movie.description == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (cover != null ? cover.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
