package io.robrose.hop.watermap;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * This class holds some odd methods that are useful for the app.
 * Created by Robert on 2/6/2016.
 */
public class Utility {
    public static void showDialogText(int resId) {
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
}
