package io.robrose.hop.watermap;

import android.content.Context;
import android.util.Log;

import com.amazonaws.geo.model.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.robrose.hop.watermap.aws.DynamoGeoClient;
import io.robrose.hop.watermap.aws.util.WaterPin;

/**
 * This class is used to fetch and format data from the database.
 */
public class FetchTests {
    private Context context;

    public FetchTests(Context context) {
        DynamoGeoClient.init();
        this.context = context;
    }

    public List<WaterPin> radiusSearch(double lat, double lng, double radius) {
        DynamoGeoClient.init();
        GeoPoint geoPoint = new GeoPoint(lat, lng);
        List<WaterPin> pins = DynamoGeoClient.getRadialPoints(geoPoint, radius);
        Log.v("Fetcher", pins.toString());
        return pins;
    }
}
