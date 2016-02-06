package io.robrose.hop.watermap;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.Bind;
import butterknife.OnClick;

public class DetailsActivity extends ActionBarActivity {
    //private Typeface robotoBold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //robotoBold = Typeface.createFromAsset(getAssets(), "Roboto-Bold.tff");

    }
}
