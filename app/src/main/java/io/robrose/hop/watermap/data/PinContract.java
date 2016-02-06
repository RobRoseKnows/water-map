package io.robrose.hop.watermap.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Robert on 2/6/2016.
 */
public class PinContract {
    public static final String CONTENT_AUTHORITY = "io.robrose.hop.watermap";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PIN = "pin";
    public static final String PATH_VIOLATION = "violation";
    public static final String PATH_LAT_LONG = "ll";
    public static final String PATH_ZIP = "zip";


    public static final class PinEntry implements BaseColumns {
        public static final String TABLE_NAME = "pins";

        public static final String COLUMN_COORD_LAT = "coord_lat";
        public static final String COLUMN_COORD_LONG = "coord_long";
        public static final String COLUMN_ZIP = "zip_code";

        public static final String COLUMN_VIOLATIONS_ID = "violations_id";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIOLATION).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PIN;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PIN;

        public static Uri buildPinUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildPinUriWithZip(int zip) {
            return BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_PIN)
                    .appendPath(PATH_ZIP)
                    .appendQueryParameter(COLUMN_ZIP, Integer.toString(zip)).build();
        }

        public static Uri buildPinUriWithLatLong(double lat, double lng) {
            return BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_PIN)
                    .appendPath(PATH_LAT_LONG)
                    .appendQueryParameter(COLUMN_COORD_LAT, Double.toString(lat))
                    .appendQueryParameter(COLUMN_COORD_LONG, Double.toString(lng))
                    .build();
        }
    }

    public static final class ViolationsEntry implements BaseColumns {

        public static final String TABLE_NAME = "violations";

        public static final String COLUMN_VIOL_CAT_CODE = "viol_cat_code";
        public static final String COLUMN_CONTAM_CODE = "contam_code";
        public static final String COLUMN_VIOL_CODE = "viol_code";
        public static final String COLUMN_DATE = "date";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIOLATION).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIOLATION;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIOLATION;


        public static Uri buildViolationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildViolationQueryWithStartDate(int zip, long startDate) {
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(zip))
                    .appendQueryParameter(COLUMN_DATE, Long.toString(startDate)).build();
        }

        public static String getLocationSettingFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static long getDateFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(2));
        }

        public static long getStartDateFromUri(Uri uri) {
            String dateString = uri.getQueryParameter(COLUMN_DATE);
            if (null != dateString && dateString.length() > 0)
                return Long.parseLong(dateString);
            else
                return 0;
        }
    }
}
