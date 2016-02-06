package io.robrose.hop.watermap.aws.util;

import com.amazonaws.geo.model.GeoPoint;

import java.util.ArrayList;

import io.robrose.hop.watermap.aws.util.WaterPin;

/**
 * Created by Robert on 2/6/2016.
 */
public class PinGroup {


    public int zipCode;
    public int longitude;
    public int latitude;
    public GeoPoint geoPoint;

    private ArrayList<WaterPin> group = new ArrayList<>();

}
