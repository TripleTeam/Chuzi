package com.want.movie.model.entities;

public enum FilterType {
    HAPPINESS(0),
    BULLETS(1),
    BRIGHTNESS(2),
    SEXUALITY(3);

    private final int position;

    FilterType(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
