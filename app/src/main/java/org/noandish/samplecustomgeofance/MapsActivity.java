package org.noandish.samplecustomgeofance;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import org.jetbrains.annotations.NotNull;
import org.noandish.library.raycastintersectpolygon.*;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, CustomGeofance.OnEnterRangePolygonListener {

    private GoogleMap mMap;

    // Points PolyGone
    private ArrayList<LatLng> points = new ArrayList<>();

    private CustomGeofance customGeofance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        customGeofance = new CustomGeofance();
        customGeofance.setOnEnterRangePolygonListener(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates("gps", 0L, 0f, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                customGeofance.changeLocation(new LatLngRange(location.getLatitude(), location.getLongitude()));

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });

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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                points.add(latLng);
                changePolygone();

            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.remove();
                for (LatLng point : points) {
                    if (point.latitude == marker.getPosition().latitude && point.longitude == marker.getPosition().longitude) {
                        points.remove(point);
                        break;
                    }
                }
                changePolygone();
                return true;
            }
        });

    }

    private Polygon polygon;

    private void changePolygone() {
        if (mMap == null)
            return;


        //   mMap.clear();
        if (polygon != null)
            polygon.remove();

        customGeofance.clear();


        if (points.size() > 0)
            polygon = mMap.addPolygon(
                    new PolygonOptions()
                            .addAll(points)
                            .fillColor(Color.parseColor("#55ff0077"))
                            .strokeWidth(2f)
                            .strokeColor(Color.RED)
            );

        ArrayList<LatLngRange> latLngRanges = new ArrayList<>();
        for (LatLng point : points) {
            mMap.addMarker(new MarkerOptions().position(point));
            latLngRanges.add(new LatLngRange(point.latitude, point.longitude));
        }

        //Sample 1 for add Range :
        /*

        customGeofance.addAllRange(new ParamsRangePolygons(latLngRanges));
*/

        //Sample 2 for add Range (with send any object):

        customGeofance.addAllRange(new ParamsRangePolygons(latLngRanges, "قرمز در مثال 2 "));

        //Sample 3 for add Range (with listener) :
/*
  customGeofance.addAllRange(new ParamsRangePolygons(latLngRanges, new OnRangePolygonListener() {
            @Override
            public void onChangeStatus(@NotNull EventRangePolygon event, @NotNull ParamsRangePolygons paramsRangePolygons) {

            }
        }));
*/
        //Sample 4 for add Range (with listener and send any object) :
             /*
   customGeofance.addAllRange(new ParamsRangePolygons(latLngRanges, new OnRangePolygonListener() {
            @Override
            public void onChangeStatus(@NotNull EventRangePolygon event, @NotNull ParamsRangePolygons paramsRangePolygons) {

            }
        }, "for example send a String object"));
*/

        //Sample 5 for add Range (with id)
        /*
        customGeofance.addAllRange(new ParamsRangePolygons(2L, latLngRanges, new OnRangePolygonListener() {
            @Override
            public void onChangeStatus(@NotNull EventRangePolygon event, @NotNull ParamsRangePolygons paramsRangePolygons) {

            }
        }, "for example send a String object"));
        */

    }


    @Override
    public void onEnteredGeo(@NotNull ParamsRangePolygons paramsRangePolygon) {
        Toast.makeText(this, "شما وارد منطقه " + paramsRangePolygon.getAny() + " شدید", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onExitedGeo(@NotNull ParamsRangePolygons paramsRangePolygon) {
        Toast.makeText(this, "شما از منطقه " + paramsRangePolygon.getAny() + " خارج شدید", Toast.LENGTH_SHORT).show();
    }


}
