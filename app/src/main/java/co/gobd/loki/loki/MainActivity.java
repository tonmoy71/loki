package co.gobd.loki.loki;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;


public class MainActivity extends FragmentActivity {

    private static final int GPS_ERRORDIALOG_REQUEST = 9001;

    // Sample data to check the location
    private static final double SEATTLE_LAT = 47.60621,
            SEATTLE_LNG = -122.33207,
            SYDNEY_LAT = -33.867487,
            SYDNEY_LNG = 151.20699,
            NEWYORK_LAT = 40.714353,
            NEWYORK_LNG = -74.005973;

    // Camera zoom level
    private static final float DEFAULT_ZOOM = 3;

    // Reference to map object
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (servicesOk()) {

            setContentView(R.layout.activity_map);

            if (initMap()) {
                Toast.makeText(this, "Ready to map!", Toast.LENGTH_SHORT).show();
                gotoLocation(SEATTLE_LAT, SEATTLE_LNG, DEFAULT_ZOOM);
            } else {
                Toast.makeText(this, "Map not available!", Toast.LENGTH_SHORT).show();
            }

        } else {
            setContentView(R.layout.activity_main);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Checks if the Google Play Service is available on the device
    public boolean servicesOk() {
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            // Error dialog will be selected automatically, user doesn't need to know
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, GPS_ERRORDIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't connect to Google Play Services", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    // Map initialization
    private boolean initMap() {
        if (mMap == null) {
            // Get reference to the map fragment
            SupportMapFragment mapFrag =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap = mapFrag.getMap();

        }
        return (mMap != null);
    }

    // Jumps to a specific location from given lat, lng
    private void gotoLocation(double lat, double lng) {
        // Constructs a LatLng object with the given Lat and Lng
        LatLng latLng = new LatLng(lat, lng);

        // Sets the camera position to this new location
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
        mMap.moveCamera(cameraUpdate);
    }

    // Jumps to a specific location from given lat, lng and zoom level
    private void gotoLocation(double lat, double lng, float zoom) {
        LatLng latLng = new LatLng(lat, lng);
        // Sets the initial camera position to this new location, with the default zoom level
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.animateCamera(cameraUpdate);
    }

    // Finds the location information using Geocoder
    public void geoLocate(View v) throws IOException {
        hideSoftKeyboard(v);

        // User input
        EditText et_location = (EditText) findViewById(R.id.inputLocation);
        String location = et_location.getText().toString();

        // Gecoder initialization
        Geocoder geocoder = new Geocoder(this);
        // Creates a list of address information, second argument is the number of max result
        List<Address> addressList = geocoder.getFromLocationName(location, 1);

        // we've set only one result, so no loop is needed.
        // if max result > 1, we need to iterate through the list
        Address address = addressList.get(0);
        String locality = address.getLocality();

        double latitude = address.getLatitude();
        double longitude = address.getLongitude();

        String country = address.getCountryName();

        gotoLocation(latitude, longitude);

        String toastMsg = "Lat: " + latitude
                + "\n" + "Lng: " + longitude
                + "\n" + "Locality: " + locality
                + "\n" + "Country: " + country;

        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

    }

    private void hideSoftKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
