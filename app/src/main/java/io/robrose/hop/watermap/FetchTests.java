package io.robrose.hop.watermap;

import android.content.Context;

import com.amazonaws.geo.model.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.robrose.hop.watermap.aws.DynamoGeoClient;
import io.robrose.hop.watermap.aws.util.PinGroup;
import io.robrose.hop.watermap.aws.util.WaterPin;

/**
 * This class is used to fetch and format data from the database.
 */
public class FetchTests {
    private Context context;
    private HashMap<Integer, PinGroup> zipCodes;

    public FetchTests(Context context) {
        DynamoGeoClient.init();
        this.context = context;
    }

    public List<WaterPin> radiusSearch(double lat, double lng, double radius) {
        GeoPoint geoPoint = new GeoPoint(lat, lng);
        List<WaterPin> pins = DynamoGeoClient.getRadialPoints(geoPoint, radius);
        return pins;
    }

//    /**
//     * This method takes a list of {@link WaterPin}'s and places them in their zip code's
//     * {@link PinGroup}. The method calls the getZipCodePinGroup method to get the proper PinGroup.
//     * @param list A list of Pins with violations collected from the server.
//     * @return A list of PinGroups by zipcode.
//     */
//    public List<PinGroup> processList(List<WaterPin> list) {
//        ArrayList<PinGroup> pinList = new ArrayList<>();
//
//        for(WaterPin pin : list) {
//            PinGroup zipCodePinGroup = getZipCodePinGroup(pin.zip, pin);
//            zipCodePinGroup.push(pin);
//            pinList.add(zipCodePinGroup);
//        }
//
//        return pinList;
//    }

//    /**
//     * This function takes a zipcode and checks to see whether or not the zip code is already in
//     * zipCodes. If it is not, it creates a new PinGroup for the zipcode. A pin is passed in to
//     * provide the initial values for the PinGroup. Function will return the PinGroup for the zip
//     * code.
//     * @param zip The zipcode of the pin.
//     * @param pin The pin to take the metadata from.
//     * @return The PinGroup for the given zipcode.
//     */
//    private PinGroup getZipCodePinGroup(int zip, WaterPin pin) {
//        PinGroup pins;
//        if(zipCodes.containsKey(zip)) {
//            pins = zipCodes.get(zip);
//        } else {
//            pins = new PinGroup(
//                    zip,
//                    pin.lat,
//                    pin.lng,
//                    pin.geoPoint,
//                    Integer.toString(zip)
//            );
//            zipCodes.put(zip, pins);
//        }
//        return pins;
//    }
}
