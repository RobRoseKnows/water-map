package io.robrose.hop.watermap;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class NotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}



//Gradle dependency added
    dependencies{
            compile 'com/gethub/rey5137:material:1.2.2'
            compile 'com.android.support:appcompat-v7:23.1.1'
            compile 'com.android.support.cardview-v7:23.1.1'
            }

// Dialog. Has app darkened in background and a popup window.
// This is may be where a scrolling option selection is after
// the field of test is selected
// in code

//com.rey.material.app.Dialog
//com.rey.material.app.SimpleDialog extends above and has common features
Dialog mDialog = new Dialog(context,styleId);
mDialog.applyStyle(styleId) //need find styleId
    .title("???The selected test field")
    //Add scroll option to form
//Text form
//com.rey.material.widget.EditText
//et_supportMode should be none

//helper looks like title but under the input
//the title is on the line where input is and is erased
//a label puts the title just above the input area when typing
//et_labelEnable puts floating label
//et_helper makes helper text
//et_supportSingleLine makes helper text one line

//maybe? answerView.setOnEditorActionListener(new TextView.OnEditorActionListener() {



    .positiveAction("OK")
    .negatieAction("CANCEL")
    .contentView(view) //??
    .cancelable(true) //??
    ... //??
    .show(); //??
    android:layout_width(match_parent)
    android:layour_height(match_parent)
    di_dimAmount(3) //3? amount of dim applied to background
    di_backgroundColor(???) //background color of dialog
    di_canceledOnTouchOutside(?true?) //if we want this





//Direct Linking to the Amazon mobile compatible webpages
// need AssociatesAPI and LinkService (I don't know how to do this)

//This goes in the AndroidManifest.xml with only INTERNET being required and others optional
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

//In your Activity.onCreate(), initialize the API by calling AssociatesAPI.initialize(config). Calling any other API method before initialization will cause a NotInitializedException

protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssociatesAPI.initialize(new AssociatesAPI.Config(APPLICATION_KEY, this));
        }
//You will need the Application Key to initialize the API. To get the Application Key, log on to Amazon Apps & Games Developer Portal,
//click on the "My Apps" tab and then select an existing app or click on the "Add a New App" button. When creating a new app, just fill in
//the "App Title" and "Form Factor" fields and click the "Save" button. The next screen will show the unique Application Key value, which
//is a 32-character globally unique alphanumeric string that is used to identify your app. The same Application Key can be used across
//platforms, but each distinct app must have a unique Application Key.


//This directs to Amazon product detail page
//I don't know if this is taking the name of the product or if we need to provide it

//Tester kit from http://www.amazon.com/First-Alert-WT1-Drinking-Water/dp/B000FBMAVQ/ref=sr_1_4?ie=UTF8&qid=1454777799&sr=8-4&keywords=water+test
        String asin = "B000FBMAVQ";
        openProductPageButton = (Button)findViewById(R.id.open_product_page_button);
        openProductPageButton.setEnabled(true);
        openProductPageButton.setOnClickListener(new View.OnClickListener() {

public void onClick(View view){
        OpenProductPageRequest request=new OpenProductPageRequest(asin);
        try{
        LinkService linkService=AssociatesAPI.getLinkService();
        linkService.openRetailPage(request)
        }catch(NotInitializedException e){
        Log.v(TAG,e.printStackTrace());
        }
        }
        });

//Filter from http://www.amazon.com/ZeroWater-ZP-010-10-Cup-Pitcher/dp/B0073PZ6O0/ref=sr_1_4?ie=UTF8&qid=1454777929&sr=8-4&keywords=water+zero
        String asin = "B0073PZ6O0";
        openProductPageButton = (Button)findViewById(R.id.open_product_page_button);
        openProductPageButton.setEnabled(true);
        openProductPageButton.setOnClickListener(new View.OnClickListener() {

public void onClick(View view){
        OpenProductPageRequest request=new OpenProductPageRequest(asin);
        try{
            LinkService linkService=AssociatesAPI.getLinkService();
            linkService.openRetailPage(request)
        }catch(NotInitializedException e){
            Log.v(TAG,e.printStackTrace());
        }
        }
        });

//Filter replacements from http://www.amazon.com/ZeroWater-ZR-017-Replacement-Filter-Pack/dp/B001CUQ1C8/ref=pd_bxgy_79_2?ie=UTF8&refRID=1M1HA2JGHM1GJFG1K0ND
        String asin = "B001CUQ1C8";
        openProductPageButton = (Button)findViewById(R.id.open_product_page_button);
        openProductPageButton.setEnabled(true);
        openProductPageButton.setOnClickListener(new View.OnClickListener() {

public void onClick(View view){
        OpenProductPageRequest request=new OpenProductPageRequest(asin);
        try{
        LinkService linkService=AssociatesAPI.getLinkService();
        linkService.openRetailPage(request)
        }catch(NotInitializedException e){
        Log.v(TAG,e.printStackTrace());
        }
        }
        });

//Treatment tablets from http://www.amazon.com/Potable-Aqua-Water-Treatment-Tablets/dp/B001949TKS/ref=sr_1_2?ie=UTF8&qid=1454777742&sr=8-2&keywords=water+purification+tablets
        String asin = "B001949TKS";
        openProductPageButton = (Button)findViewById(R.id.open_product_page_button);
        openProductPageButton.setEnabled(true);
        openProductPageButton.setOnClickListener(new View.OnClickListener() {

public void onClick(View view){
        OpenProductPageRequest request=new OpenProductPageRequest(asin);
        try{
        LinkService linkService=AssociatesAPI.getLinkService();
        linkService.openRetailPage(request)
        }catch(NotInitializedException e){
        Log.v(TAG,e.printStackTrace());
        }
        }
        });

//This is a template layout for something similar to the Google Maps app.

//add to build.gradle
dependences{
        repositories{
            mavenCentral()
        }
        compile 'com.sothree.slidinguppanel:library:3.2.1'
        }
//Usage
//•Include  com.sothree.slidinguppanel.SlidingUpPanelLayout  as the root element in your activity layout.
//set gravity to bottom, maybe layout_gravity
//child for main layout and a child for sliding up panel layout
//main has width and height to match_parent
//sliding has witdh to match_parent and height to desired by using setAnchorPoint to middle of screen or setPanelHeight method or umanoPanelHeight attribute
//define a layout_weight for the sliding view.
//by default the whole panel will act as a drag region and will intercept clicks and drag events.
//can adjust by using setDragView method or unmanoDragView attribute
//default pushed up the main content. can set to overlay by setOverlayed method or umanoOverlay attribute

//
//


//Text form
//com.rey.material.widget.EditText
//et_supportMode should be none

//helper looks like title but under the input
//the title is on the line where input is and is erased
//a label puts the title just above the input area when typing
//et_labelEnable puts floating label
//et_helper makes helper text
//et_supportSingleLine makes helper text one line


