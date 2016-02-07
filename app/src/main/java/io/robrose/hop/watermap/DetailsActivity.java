package io.robrose.hop.watermap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.robrose.hop.watermap.aws.util.WaterPin;

public class DetailsActivity extends ActionBarActivity implements OnMapReadyCallback {
    private WaterPin mWaterPin;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private Marker mMarker;
    private int contamCode;

    @Bind(R.id.hazard_type_iv)
    ImageView hazardImageView;
    @Bind(R.id.explain_text)
    TextView explainTextView;
    @Bind(R.id.hazard_text)
    TextView hazardTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle bundle = getIntent().getExtras();
        mWaterPin = bundle.getParcelable(Utility.BUNDLE_PIN);

        contamCode = Integer.parseInt(mWaterPin.contamCode);
        hazardImageView.setImageResource(findIcon(contamCode));
        explainTextView.setText(findExplanation(contamCode));
        hazardTextView.setText(findHealthId(mWaterPin.majorRisk));

        mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        this.setTitle(mWaterPin.name);
        ButterKnife.bind(this);
    }

    private int findIcon(int code) {
        switch (code / 1000) {
            case 1: {                   // Inrganic Chemical
                return R.drawable.ic_beaker_black_48dp;
            }
            case 2: {                   // Organic Chemical
                return R.drawable.ic_barrel_black_48dp;
            }
            case 3: {                   // Biohazard
                return R.drawable.ic_biohazard_black_48dp;
            }
            case 4: {                   // Radioactive
                return R.drawable.ic_radioactive_black_48dp;
            }
            case 5: {                   // Heavy metal
                return R.drawable.ic_weight_black_48dp;
            }
            default: {
                return R.drawable.ic_pencil_black_48dp;
            }
        }
    }

    private int findExplanation(int code) {
        switch (code / 1000) {
            case 1: {                   // Inrganic Chemical
                return R.string.inorganic_explanaton;
            }
            case 2: {                   // Organic Chemical
                return R.string.organic_explanation;
            }
            case 3: {                   // Biohazard
                return R.string.biohazard_explanation;
            }
            case 4: {                   // Radioactive
                return R.string.radiation_explanation;
            }
            case 5: {                   // Heavy metal
                return R.string.heavymetal_explanation;
            }
            default: {
                return R.string.no_explanation;
            }
        }
    }

    private int findHealthId(boolean yn) {
        if (yn) {
            return R.string.no_health;
        } else {
            return R.string.yes_health;
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(mWaterPin.lat, mWaterPin.lng))
                .title(mWaterPin.name);
        mMarker = mMap.addMarker(markerOptions);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
    }
}
