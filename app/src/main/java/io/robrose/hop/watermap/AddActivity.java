package io.robrose.hop.watermap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

import butterknife.Bind;
import butterknife.OnClick;
import io.robrose.hop.watermap.aws.util.WaterPin;


/**
 * Created by Robert on 2/7/2016.
 */
public class AddActivity extends AppCompatActivity {
public void tolead(View view){
    //Do something in response to button
    Intent i = new Intent(this, LeadActivity.class);
    startActivity(i);
}
    public void tobacteria(View view){
        //Do something in response to button
        Intent i = new Intent(this, BacteriaActivity.class);
        startActivity(i);
    }
}