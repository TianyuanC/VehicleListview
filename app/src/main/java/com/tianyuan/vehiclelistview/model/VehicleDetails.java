package com.tianyuan.vehiclelistview.model;

import android.graphics.Bitmap;

/**
 * Created by churyan on 16-03-06.
 */
public class VehicleDetails {
    private Structures structures;
    private double price;
    private String imageUrl;
    private String description;
    private Bitmap bitmap;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Structures getStructures() {
        return structures;
    }

    public void setStructures(Structures structures) {
        this.structures = structures;
    }

    public double getPrice() {
        return price;
    }

    public String getPriceFormat() {
        return "$" + price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return structures.toString() + "\n" + price + "\n" + imageUrl + "\n\n";
    }
}
