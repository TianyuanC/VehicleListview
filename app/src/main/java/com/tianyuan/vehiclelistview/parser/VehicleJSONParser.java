package com.tianyuan.vehiclelistview.parser;

import com.tianyuan.vehiclelistview.model.Vehicle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by churyan on 16-03-06.
 */
public class VehicleJSONParser {
    public static List<Vehicle> parseFeed(String data){

        List<Vehicle> vehicleList = null;
        try {
            JSONObject response = new JSONObject(data);
            JSONArray arr = response.getJSONArray("Hits");
            vehicleList = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Vehicle vehicle = new Vehicle();
                vehicle.setId(obj.getInt("Id"));
                vehicle.setForeignId(obj.getInt("ForeignId"));
                vehicle.setSourceId(obj.getInt("SourceId"));
                vehicle.setDetails(VehicleDetailXMLParser.parseDetails(obj.getString("DocumentXml")));
                vehicleList.add(vehicle);
            }
            return vehicleList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
