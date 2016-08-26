package com.pclub.arnavigation;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.Sitepoint.NewUnityProject1.UnityPlayerNativeActivity;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.unity3d.player.UnityPlayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class NavigationActivity extends UnityPlayerNativeActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,LocationListener {
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    private int I=0;
    private MapFragment mapFragment;
    private Marker marker;
    Location start=new Location("");
    Location current=new Location("");
    String finalDestination;
    String startAddress;
    List<GDPath> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i=getIntent();
        extrdata obj= new extrdata();
        i.getDoubleExtra("lat",0.00);
        start.setLatitude(i.getDoubleExtra("lat",0.00));
        start.setLongitude(i.getDoubleExtra("long",0.00));
        obj.execute(i.getStringExtra("placeid"),((Double)start.getLatitude()).toString(),((Double)start.getLongitude()).toString());
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
        }
        googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addApi(AppIndex.API).build();
        locationRequest=new LocationRequest()
                .setInterval(5000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient,
                builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                final LocationSettingsStates locationSettingsStates=locationSettingsResult.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.v("Location settings","resolution required here settings on mobile");
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.v("Location Settings","big error cannot get settings");
                        break;
                }
            }
        });
        setContentView(R.layout.activity_navigation);
        FrameLayout layout = (FrameLayout) findViewById(R.id.frame);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT);
        layout.addView(mUnityPlayer.getView(), 0, lp);
        mUnityPlayer.requestFocus();
        this.mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.small_map);
        this.mapFragment.getMapAsync(this);
         //layout.addView(findViewById(R.id.mapcontainer));
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
    public void startLocationUpdates(){
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);
    }
    @Override
    public void onConnectionSuspended(int cause) {
    }
    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();

    }
    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }
    @Override
    public void onLocationChanged(Location location1) {
        Location location = location1;
        if((current.getLongitude()-location.getLongitude()>0.0001)||(current.getLatitude()-location.getLatitude()>0.0001)){
            Log.v("location changed","***************************************"+location.getLatitude());
            //animateMarker(marker,new LatLng(location.getLatitude(),location.getLongitude()),true);
            //marker.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),15.00f));
            current=location;
        }
        if(data==null)
            return;
        int pathsize=data.get(I).getPath().size()-1;
        List<GDPoint> path= data.get(I).getPath();
        Location lo= new Location("");
        lo.setLatitude(path.get(pathsize).mLat);
        lo.setLongitude(path.get(pathsize).mLng);
        Log.v("bhjbhjijbhjb",""+location.getAccuracy()+"*********************");
        if(I==0){
                TextView txt = (TextView) findViewById(R.id.html_text);
                txt.setText(Html.fromHtml(data.get(I).getHtmlText()));
        }
        if(location.distanceTo(lo)<=location.getAccuracy()) {
            TextView txt = (TextView) findViewById(R.id.html_text);
            txt.setText(Html.fromHtml(data.get(I).getHtmlText()));
            I++;
        }
        Log.v("location changed",""+location.getLongitude()+" "+location.getLatitude());
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
        Log.v("fine access permission","activity running----------------------------------------------------------------");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.pclub.arnavigation/http/host/path")
        );
        Log.v("fine access permission","activity running----------------------------------------------------------------");
        AppIndex.AppIndexApi.start(googleApiClient, viewAction);
        Log.v("fine access permission","activity running----------------------------------------------------------------");
    }
    @Override
    protected void onStop() {
        Log.v("fine access permission","activity running----------------------------------------------------------------");
        googleApiClient.disconnect();
        Log.v("fine access permission","activity running----------------------------------------------------------------");
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.pclub.arnavigation/http/host/path")
        );
        AppIndex.AppIndexApi.end(googleApiClient, viewAction);
    }
    public class extrdata extends AsyncTask<String, Void, String> {
        String json=null;

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL web = new URL("https://maps.googleapis.com/maps/api/directions/json?origin="+params[1]+","+params[2]+"&destination=place_id:"+params[0]+"&key=AIzaSyC0NvcqqxoLqk1R6BzRsmU3f7V9-rMmWRQ");
                connection = (HttpURLConnection) web.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0)
                    return null;
                json = buffer.toString();
                Log.v("json stirng",web.toString());


            } catch (IOException e) {
                Log.e("AsyncTAsk", "error", e);
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("ForecastFragment", "Error closing stream", e);
                    }
                }

            }Log.v("onOptionsItemSelected","refresh success");
            return json;
        }
        @Override
        public void onPostExecute(String result){


            try{
                JsonParser parser=new JsonParser();
                List<GDirection> directionList= parser.parse(new JSONObject(result));
                startAddress=directionList.get(0)
                                .getLegsList().get(0)
                                .getmStartAddress();
                current=start;
                finalDestination=directionList.get(0)
                                    .getLegsList().get(0)
                                    .getmEndAddress();
                data=directionList.get(0).getLegsList().get(0).getPathsList();
                marker=mMap.addMarker(new MarkerOptions().position(new LatLng(start.getLatitude(),start.getLongitude())));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(start.getLatitude(),start.getLongitude()),15.00f));
                for(GDPath path:data) {
                List<GDPoint> list=path.getPath();
                    for (int i = 0; i < list.size()-1; i++)
                        mMap.addPolyline(new PolylineOptions()
                                .add(list.get(i).getLatLng(),list.get(i+1).getLatLng())
                                .width(10.0f).color(Color.BLUE).geodesic(true));

                }
            }
            catch(JSONException j){
                Log.e("onCreeateView","Json exception",j);
            }
        }

    }
}
