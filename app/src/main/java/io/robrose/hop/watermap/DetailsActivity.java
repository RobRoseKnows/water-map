package io.robrose.hop.watermap;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.Bind;
import butterknife.OnClick;
import io.robrose.hop.watermap.aws.util.PinGroup;

public class DetailsActivity extends ActionBarActivity {
    private PinGroup mPinGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        

        //robotoBold = Typeface.createFromAsset(getAssets(), "Roboto-Bold.tff");

    }
}
