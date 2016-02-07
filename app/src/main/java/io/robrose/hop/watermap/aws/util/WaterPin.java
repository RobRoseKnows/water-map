package io.robrose.hop.watermap.aws.util;

import com.amazonaws.geo.model.GeoPoint;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

/**
 * Created by Robert C. on 2/6/2016.
 */
public class WaterPin {

    public String uuid;
    public String name;

    public String violCatCode;
    public String contamCode;
    public String violCode;
    public boolean majorRisk;
    public boolean majorViolation;

    public double lat;
    public double lng;
    public GeoPoint geoPoint;

    public String addressLineOne;
    public String addressLineTwo;
    public String cityName;
    public String stateId;
    public int zip;

    public WaterPin(Map<String, AttributeValue> map) {
        violCatCode = map.get(Constants.FIELD_VIOL_CAT).getS();
        contamCode = map.get(Constants.FIELD_CONT_CODE).getS();
        violCode = map.get(Constants.FIELD_VIOL_CODE).getS();

        majorRisk = buildBooleanFromString(map.get(Constants.FIELD_MAJOR_RISK).getS());
        majorViolation = buildBooleanFromString(map.get(Constants.FIELD_MAJOR_VIOL).getS());

        addressLineOne = map.get(Constants.FIELD_ADDRESS_ONE).getS();
        addressLineTwo = map.get(Constants.FIELD_ADDRESS_TWO).getS();

        name = addressLineOne + " " + cityName + ", " + stateId;
    }

    public boolean buildBooleanFromString(String s) {
        switch(s.toUpperCase()) {
            case "Y": {
                return true;
            }
            case "N": {
                return false;
            }
            default: {
                return false;
            }
        }
    }

    public String buildFullAddress(boolean newLines) {
        if (newLines) {
            return addressLineOne + "\n" +
                    buildAddressLineTwo(newLines) +
                    cityName + ", " + stateId + "\n" +
                    zip;
        } else {
            return addressLineOne + " " +
                    buildAddressLineTwo(newLines) +
                    cityName + ", " + stateId + " " +
                    zip;
        }
    }

    private String buildAddressLineTwo(boolean newLines) {
        if(newLines) {
            if (addressLineTwo != null || addressLineTwo != "") {
                return addressLineTwo + "\n";
            }
        } else {
            if (addressLineTwo != null || addressLineTwo != "") {
                return addressLineTwo + " ";
            }
        }
        return "";
    }
}
