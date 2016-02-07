package io.robrose.hop.watermap.aws.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.amazonaws.geo.model.GeoPoint;

import java.util.ArrayList;

import io.robrose.hop.watermap.aws.util.WaterPin;

/**
 * The PinGroup class holds all the pins from a given zipcode.
 */
public class PinGroup implements Parcelable{


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(zipCode);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writ
    }
}
