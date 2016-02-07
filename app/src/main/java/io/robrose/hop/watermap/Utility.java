package io.robrose.hop.watermap;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

/**
 * This class holds some odd methods that are useful for the app.
 * Created by Robert on 2/6/2016.
 */
public class Utility {
    public static final int PERMISSION_REQUEST_LAST_LOCATION = 2;
    public static final String BUNDLE_GROUP_NUMBER = "num";
    public static final String BUNDLE_PIN = "pin";

    /**
     * This function shows text in a dialog box from a given string resId and the app context.
     * @param resId String to display.
     * @param context App context.
     */
    public static void showDialogText(int resId, Context context) {
        // Show them a dialog with the rationale.
        AlertDialog.Builder rationaleAlert = new AlertDialog.Builder(context);
        rationaleAlert.setMessage(resId);
        rationaleAlert.setTitle(R.string.app_name);
        rationaleAlert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        rationaleAlert.setCancelable(true);
        rationaleAlert.create().show();

    }

    /**
     * This method handles the permission rationale and requests from other methods in other classes.
     * Calls the relevant permission request and does stuff.
     * @param context The activity with the callback functions.
     * @param permission The permission to request.
     * @param requestId The requestId within the context activity.
     */
    public static void permissionRationaleAndRequest(Activity context, String permission, int requestId) {
        if(ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {

            // Show relevant rationale based on requestId.
            switch(requestId) {
                case PERMISSION_REQUEST_LAST_LOCATION: {
                    Utility.showDialogText(R.string.location_permission_rationale, context);
                    break;
                }
            }

            // Now request permission
            ActivityCompat.requestPermissions(
                    context,
                    new String[]{permission},
                    requestId);
        } else {
            ActivityCompat.requestPermissions(
                    context,
                    new String[]{permission},
                    requestId);
        }

    }
}
