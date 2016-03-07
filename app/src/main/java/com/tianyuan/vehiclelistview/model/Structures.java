package com.tianyuan.vehiclelistview.model;

/**
 * Created by churyan on 16-03-06.
 */
public class Structures {
    private int year;
    private String make;
    private String model;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return year + " " + make + " " + model;
    }
}
