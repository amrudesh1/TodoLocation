package amrudesh.com.locationtodoreminder.geofence;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseGeofire  {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("Locations");
    GeoFire geoFire = new GeoFire(databaseReference);
    private Context Context;
    GeoQuery geoQuery;

    public void geoFireImpementer(String id,String latitude,String longitude) {
        try {
            geoFire.getLocation(id, new LocationCallback() {
                @Override
                public void onLocationResult(String key, GeoLocation location) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            String s;
            geoQuery = geoFire.queryAtLocation(new GeoLocation(Double.valueOf(latitude), Double.valueOf(longitude)), 0.6);
            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(String key, GeoLocation location) {
                    Log.i("Location", "Location Entered");
                }

                @Override
                public void onKeyExited(String key) {

                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {

                }

                @Override
                public void onGeoQueryReady() {

                }

                @Override
                public void onGeoQueryError(DatabaseError error) {

                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
