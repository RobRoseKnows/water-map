package io.robrose.hop.watermap.aws.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.amazonaws.geo.model.GeoPoint;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

/**
 * Created by Robert C. on 2/6/2016.
 */
public class WaterPin implements Parcelable{

    private static final double PIN_VARIANCE  = .01;

    public String uuid;
    public String name;

    public String violCatCode;
    public String contamCode;
    public String violCode;
    public boolean majorRisk;
    public boolean majorViolation;

    public double lat;
    public double latOriginal;
    public double lng;
    public double lngOriginal;
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
        cityName = map.get(Constants.FIELD_CITY).getS();
        stateId = map.get(Constants.FIELD_STATE).getS();

        name = addressLineOne + " " + cityName + ", " + stateId;
    }

    public WaterPin(String addressLineOne,
                    String cityName,
                    String stateId,
                    boolean majorRisk,
                    String contamCode,
                    int zip,
                    double lat,
                    double lng) {
        this.addressLineOne = addressLineOne;
        this.cityName = cityName;
        this.stateId = stateId;
        this.majorRisk = majorRisk;
        this.contamCode = contamCode;
        this.zip = zip;
        this.lat = lat;
        this.lng = lng;

        this.uuid = "";
        this.name =  addressLineOne + " " + cityName + ", " + stateId;;

        this.violCatCode = "";
        this.violCode = "pi";
        this.majorRisk = false;

        this.addressLineTwo = "";
    }

    public WaterPin(Parcel in) {
        String[] data = new String[14];

        in.readStringArray(data);
        this.uuid = data[0];
        this.name = data[1];

        this.violCatCode = data[2];
        this.contamCode = data[3];
        this.violCode = data[4];
        this.majorRisk = Boolean.parseBoolean(data[5]);
        this.majorViolation = Boolean.parseBoolean(data[6]);

        this.lat = Double.parseDouble(data[7]);
//        this.lat = (this.latOriginal - PIN_VARIANCE/2) + (Math.random() * PIN_VARIANCE);

        this.lng = Double.parseDouble(data[8]);

        this.addressLineOne = data[9];
        this.addressLineTwo = data[10];
        this.cityName = data[11];
        this.stateId = data[12];
        this.zip = Integer.getInteger(data[13]);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.uuid,
                this.name,

                this.violCatCode,
                this.contamCode,
                this.violCode,
                Boolean.toString(this.majorRisk),
                Boolean.toString(this.majorViolation),

                Double.toString(this.lat),
                Double.toString(this.lng),

                this.addressLineOne,
                this.addressLineTwo,
                this.cityName,
                this.stateId,
                Integer.toString(this.zip)
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public WaterPin createFromParcel(Parcel in) {
            return new WaterPin(in);
        }

        @Override
        public WaterPin[] newArray(int size) {
            return new WaterPin[size];
        }
    };
}
