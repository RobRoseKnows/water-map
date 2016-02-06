package io.robrose.hop.watermap.aws.util;

import com.amazonaws.geo.model.GeoPoint;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

/**
 * Created by Robert C. on 2/6/2016.
 */
public class WaterPin {

        public String violCatCode;
        public String contamCode;
        public String violCode;
        public String uuid;
        public double lat;
        public double lng;
        public int zip;
        public GeoPoint geoPoint;

        public WaterPin(Map<String,AttributeValue> map){

        }
}
