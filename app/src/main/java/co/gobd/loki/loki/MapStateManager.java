package co.gobd.loki.loki;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Fahim Masud Choudhury on 1/4/2015.
 */

// Save map state after activity onCreate, onResume and screen rotation
public class MapStateManager {

    private static final String LONGITUDE = "longitude";

    private static final String LATITUDE = "latitude";

    private static final String ZOOM = "zoom";

    private static final String BEARING = "bearing";

    private static final String TILT = "tilt";

    private static final String MAPTYPE = "MAPTYPE";

    private static final String PREFS_NAME = "mapCameraState";

    private SharedPreferences mapStatePrefs;

    public MapStateManager(Context context) {
        // Sets the SharedPreference
        mapStatePrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Save map state on shared preference
    public void saveMapState(GoogleMap map) {
        SharedPreferences.Editor editor = mapStatePrefs.edit();
        CameraPosition position = map.getCameraPosition();

        // Sets the position value in the shared prefs.
        editor.putFloat(LATITUDE, (float) position.target.latitude);
        editor.putFloat(LONGITUDE, (float) position.target.longitude);
        editor.putFloat(ZOOM, position.zoom);
        editor.putFloat(TILT, position.tilt);
        editor.putFloat(BEARING, position.bearing);
        editor.putInt(MAPTYPE, map.getMapType());

        // Updates the sharedPrefs value
        editor.commit();

    }

    // Gets the current camera position saved in sharedPrefs
    public CameraPosition getSavedCameraPosition() {
        // If sharedPrefs does not have the value, return 0 as default
        double latitude = mapStatePrefs.getFloat(LATITUDE, 0);
        if (latitude == 0) {
            return null;
        }
        double longitude = mapStatePrefs.getFloat(LONGITUDE, 0);

        // LatLng object to use in camera position
        LatLng target = new LatLng(latitude, longitude);

        float zoom = mapStatePrefs.getFloat(ZOOM, 0);
        float bearing = mapStatePrefs.getFloat(BEARING, 0);
        float tilt = mapStatePrefs.getFloat(TILT, 0);

        // Creates a new camera position
        CameraPosition position = new CameraPosition(target, zoom, tilt, bearing);

        return position;
    }

    // Gets the map type
    // If no saved map type in sharedPrefs, return NORMAL map type as default
    public int getMapType() {
        int mapType = mapStatePrefs.getInt(MAPTYPE, GoogleMap.MAP_TYPE_NORMAL);
        return mapType;
    }

}
