package com.example.oliverasker.skywarnmarkii.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.oliverasker.skywarnmarkii.MapUtility;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener {
    private static final String TAG = "MapsActivity";
    //  Variables for controlling camera bounds, view
    LatLngBounds.Builder builder;
    CameraUpdate cameraUpdate;
    MarkerOptions markerOptions;
    private GoogleMap mMap;
    //  Holds values passed into it from QueryLauncherActivity class
    private SkywarnWSDBMapper[] receivedData;
    //  Holds value for a single report from ViewReportActivity class
    private SkywarnWSDBMapper report;
    //  For multiple reports
    // Resource: http://stackoverflow.com/questions/13855049/how-to-show-multiple-markers-on-mapfragment-in-google-map-api-v2
    private List<MarkerOptions> markerOptionsList = new ArrayList<MarkerOptions>();
    //For Single report
    private LatLng pos;
    private boolean singleReport;

    private ArrayList<SkywarnWSDBMapper> reportsWithValidCoords;
    private ArrayList<String> snippetList;

    //For getting curernt location
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        snippetList = new ArrayList<>();
        //Convert report list into LtLng then into MarkerOptions
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ///////////////////////////////////////////////////
            //          Multiple Items from queryList       //
            //////////////////////////////////////////////////
            //  Checks if maps activity is being called from QueryLauncherActivity which will show all reports on map
            if (bundle.getSerializable("reportList") != null) {
                singleReport = false;
                receivedData = (SkywarnWSDBMapper[]) bundle.getSerializable("reportList");
                Log.d(TAG, "data.length:  " + receivedData.length);

                //Todo: (USED FOR TESTING) FINISH REMOVE BELOW ITERATION
                for (SkywarnWSDBMapper s : receivedData) {
                    if (s != null)
                        Log.d(TAG, "receivedReports?: lat: " + s.getLatitude() + " long: " + s.getLatitude());
                }

                reportsWithValidCoords = removeReportsWithInvalidCoords(receivedData);
                for (SkywarnWSDBMapper report : reportsWithValidCoords) {
                    int resource = 0;
                    switch (report.getWeatherEvent()) {
                        case "Winter Event":
                            resource = R.drawable.snow_icon;
                            break;
                        case "Severe Event":
                            resource = R.drawable.severe;
                            break;
                        case "Coastal Event":
                            resource = R.drawable.coastal;
                            break;
                        case "Rain/Flood Event":
                            resource = R.drawable.rain;
                            break;
                        default:
                            resource = R.drawable.sunny;
                    }
                    LatLng ll = new LatLng(report.getLatitude(), report.getLongitude());
                    // Read more: http://www.androidhub4you2.com/2015/06/android-maximum-zoom-in-google-map.html#ixzz4WXzS6wh6
                    Log.d(TAG, "latlong: " + ll.toString());
                    Bitmap icon = resizeBitmapForMarker(resource);
                    markerOptions = new MarkerOptions()
                            .position(ll)
                            .snippet(report.getComments())
//                            .icon(BitmapDescriptorFactory.fromResource(resource))
                            .icon(BitmapDescriptorFactory.fromBitmap(icon))
                            .title(createMarkerTitle(report));
                    markerOptionsList.add(markerOptions);
                }
            }
        }


        ///////////////////////////////////////////////////
        //       Single Item from ViewReportActivity    //
        //////////////////////////////////////////////////
        //  Checks if maps activity is being called from ViewReportActivity which
        //  will show single selected report on map
        if (bundle.getSerializable("report") != null) {
           // Log.d(TAG, "report!=null");
            report = (SkywarnWSDBMapper) bundle.getSerializable("report");
            singleReport = true;

            String title = createMarkerTitle(report);
            Log.d(TAG, "title: " + title);
            //if(report.getLatitude() !=9999  && report.getLongitude() != 9999) {
            Log.i(TAG, "lat:: "+String.valueOf(report.getLatitude()));
               // LatLng ll = new LatLng(report.getLatitude(), report.getLongitude());


            LatLng ll = MapUtility.getLocationFromAddress(this,title);
            if (ll != null) {
                markerOptions = new MarkerOptions()
                        .title(createMarkerTitle(report))
                        .position(ll);

                markerOptionsList.add(markerOptions);
                //}

            }
        }
    }

    //Remove all reports from list with invalid coordinates
    private ArrayList<SkywarnWSDBMapper> removeReportsWithInvalidCoords(SkywarnWSDBMapper[] reportArray){
        ArrayList<SkywarnWSDBMapper> reportsWithValidCoords = new ArrayList<>();
        for (int i = 0; i < reportArray.length; i++) {
            if (reportArray[i].getLatitude() != 9999 && reportArray[i].getLongitude() != 9999) {
                Log.d(TAG, " Valid Coords: lat: " + reportArray[i].getLatitude() + " long: " + reportArray[i].getLongitude());
                LatLng ll = new LatLng(reportArray[i].getLatitude(), reportArray[i].getLongitude());
                reportsWithValidCoords.add(reportArray[i]);
            }
        }
        return reportsWithValidCoords;
    }

    public Bitmap resizeBitmapForMarker(int resourceID) {
        int height = 100;
        int width = 100;
       // BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.mipmap.marker);
        //Bitmap b=bitmapdraw.getBitmap();
        Bitmap b = BitmapFactory.decodeResource(getApplicationContext().getResources(), resourceID);
        Bitmap icon = Bitmap.createScaledBitmap(b, width, height, false);

        return  icon;
    }

    private String createMarkerTitle(SkywarnWSDBMapper report) {
        StringBuilder title = new StringBuilder("");

        if (report.getStreet().trim() != "|" )
            title.append(report.getStreet() + ", ");
        if (report.getEventCity().trim() != "|")
            title.append(report.getEventCity() + ", ");
        if (report.getEventState().trim() != "|")
            title.append(report.getEventState());
        Log.d(TAG, "createMarkerTitleOutput(): "+title.toString());
        return title.toString();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    //Resource for finding location
    // http://stackoverflow.com/questions/2227292/how-to-get-latitude-and-longitude-of-the-mobile-device-in-android
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        // Adds zoom buttons to map
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // Adds compas to map
        mMap.getUiSettings().setCompassEnabled(true);

        //  Draw markers on map
      //  for(MarkerOptions m : markerOptionsList){
        for(int i =0; i < markerOptionsList.size();i++){
            String snippet = reportsWithValidCoords.get(i).getWeatherEvent() + ": "+ reportsWithValidCoords.get(i).getComments();
            snippetList.add(snippet);

            drawMarker(markerOptionsList.get(i).snippet(snippet));
        }
        Log.d(TAG, "markerOptionsList size(): " + markerOptionsList.size());
    }


    //Draws marker on map using latLng
    private void drawMarker(LatLng latLng){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        mMap.addMarker(markerOptions);
    }

//      Overloaded method that Draws marker on map using MarkerOptions
//      Prefer this method bc you can customize MarkerOptions with report details
//      prior to sending to this method that does not have acce
    private void drawMarker(MarkerOptions m){
        mMap.addMarker(m);
        if(singleReport)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(), 14.0f));
        else
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(), 8.0f));
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(this, ViewReportActivity.class);
        SkywarnWSDBMapper selectedReport=null;
//        intent.putExtra("selectedReport", marker);
//            if(marker.equals() )
        for(int i =0; i < snippetList.size();i++){
            if(marker.getSnippet().equals(markerOptionsList.get(i).getSnippet())){
                selectedReport=reportsWithValidCoords.get(i);
                Log.d(TAG, "onInfoWindowClick(): matching report: " + selectedReport.getWeatherEvent() + " numImages: "  + selectedReport.getNumberOfImages() );
                intent.putExtra("selectedReport", selectedReport);
                startActivity(intent);
                break;
            }
        }
//        intent.putExtra("selectedReport", selectedReport);
//        Log.d(TAG, "onInfoWindoClick() marker ID: " + marker.getId() + " position: " + marker.getPosition());
//            startActivity(intent);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
