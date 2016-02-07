package io.robrose.hop.watermap.aws.util;

import com.amazonaws.geo.model.GeoPoint;

import java.util.ArrayList;

import io.robrose.hop.watermap.aws.util.WaterPin;

/**
 * The PinGroup class holds all the pins from a given zipcode.
 */
public class PinGroup {


    public int zipCode;
    public double latitude;
    public double longitude;
    public GeoPoint geoPoint;
    public String name;

    private ArrayList<WaterPin> group = new ArrayList<>();

    public PinGroup(int zipCode, double latitude, double longitude, GeoPoint geoPoint, String name) {
        this.zipCode = zipCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.geoPoint = geoPoint;
        this.name = name;
    }

    public void push(WaterPin pin) {
        group.add(pin);
    }

    public WaterPin pull(int i) {
        return group.get(i);
    }

    public ArrayList<WaterPin> getGroup() {
        return group;
    }

}
