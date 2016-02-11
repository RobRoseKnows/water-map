package io.robrose.hop.watermap;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import io.robrose.hop.watermap.aws.DynamoGeoClient;

/**
 * Created by Laila on 007,2,7.
 */
public class LeadActivity extends AppCompatActivity {
    public void sendLeadMessPos(View view){
        String outLead = "Y";
        DynamoGeoClient.insertPoint(MainActivity.mLastLocation.getLatitude(), MainActivity.mLastLocation.getLongitude(), "Lead", outLead);
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void sendLeadMessNeg(View view){
        String outLead = "N";
        DynamoGeoClient.insertPoint(MainActivity.mLastLocation.getLatitude(), MainActivity.mLastLocation.getLongitude(), "Lead", outLead);
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}