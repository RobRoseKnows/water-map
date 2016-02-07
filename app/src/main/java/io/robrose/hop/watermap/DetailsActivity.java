package io.robrose.hop.watermap;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

import butterknife.Bind;
import butterknife.OnClick;
import io.robrose.hop.watermap.aws.util.WaterPin;

public class DetailsActivity extends ActionBarActivity {
    private WaterPin mWaterPin;

    @Bind(R.id.detail_name_textview) TextView detailName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle bundle = getIntent().getExtras();
        mWaterPin = bundle.getParcelable(Utility.BUNDLE_PIN);

        this.setTitle(mWaterPin.name);
    }
}
