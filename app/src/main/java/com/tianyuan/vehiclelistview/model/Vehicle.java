package com.tianyuan.vehiclelistview.model;

/**
 * Created by churyan on 16-03-06.
 */
public class Vehicle {
    private int id;
    private int sourceId;
    private int foreignId;
    private VehicleDetails details;

    public VehicleDetails getDetails() {
        return details;
    }

    public void setDetails(VehicleDetails details) {
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public int getForeignId() {
        return foreignId;
    }

    public void setForeignId(int foreignId) {
        this.foreignId = foreignId;
    }

    @Override
    public String toString() {
        return id + "\n" + details.toString();
    }
}
