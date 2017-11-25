package com.want.movie.model.entities;

public class Filter {
    private int happiness;
    private int bullets;
    private int brightness;
    private int sexuality;

    public Filter(int happiness, int bullets, int brightness, int sexuality) {
        this.happiness = happiness;
        this.bullets = bullets;
        this.brightness = brightness;
        this.sexuality = sexuality;
    }

    public int getHappiness() {
        return happiness;
    }

    public int getBullets() {
        return bullets;
    }

    public int getBrightness() {
        return brightness;
    }

    public int getSexuality() {
        return sexuality;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }

    public void setBullets(int bullets) {
        this.bullets = bullets;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public void setSexuality(int sexuality) {
        this.sexuality = sexuality;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "happiness=" + happiness +
                ", bullets=" + bullets +
                ", brightness=" + brightness +
                ", sexuality=" + sexuality +
                '}';
    }
}
