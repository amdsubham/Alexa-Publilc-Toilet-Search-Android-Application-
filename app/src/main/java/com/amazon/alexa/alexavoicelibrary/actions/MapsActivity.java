package com.amazon.alexa.alexavoicelibrary.actions;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazon.alexa.alexavoicelibrary.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.LocationRequest;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    int radius=1;
    boolean toiletfound=false;
    private float locations[];
    private String uids[];
    int flag,i=0,j=0,kite;

    private int counter=0;
    LatLng toilet;
    private Map Keys;

    private Button gettoilet ;
    private String toiletid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Keys=new HashMap();
        gettoilet=(Button)findViewById( R.id.gettoilet );
        gettoilet.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("customerRequest");
                LatLng pickupLocation;
                GeoFire geoFire = new GeoFire(ref);
                geoFire.setLocation("user", new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));

                pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Toilet Here"));
                fetchkeys();


            }
        } );







    }
    private void fetchkeys()
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Toilets");

        GeoFire geoFire=new GeoFire(reference);
        final GeoQuery geoQuery=geoFire.queryAtLocation(new GeoLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude()),radius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (radius<10)
                {
                    toiletfound=true;
                    toiletid=key;
                    toilet=new LatLng(location.latitude,location.longitude);
                    flag=Keys.size();
                    Keys.put(toiletid,toilet);

                    if(Keys.size()==flag && counter==0)
                    {
                        getdistance();

                        geoQuery.removeAllListeners();
                        counter=1;
                        return;
                    }



                }
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
                if (radius < 10) {
                    radius += 1;
                    fetchkeys();
                }

            }
        });

    }

    private  void getdistance()
    {
        Location loc2 = new Location("");
        Location loc1 = new Location("");
        locations=new float[Keys.size()];
        uids=new String[Keys.size()];
        try {
            for (Object key : Keys.keySet()) {
                uids[i] = key.toString();
                loc1.setLatitude(mLastLocation.getLatitude());
                loc1.setLongitude(mLastLocation.getLongitude());
                LatLng x = (LatLng) Keys.get(key);
                loc2.setLatitude(x.latitude);
                loc2.setLongitude(x.longitude);
                float distance = loc1.distanceTo(loc2);
                locations[i]=distance;
                i = i + 1;
            }
        }
        catch (NullPointerException e)
        {
            Log.d("nullll",e.toString());
        }
        try {
            sortuids();
        }
        catch (NullPointerException e){
            fetchkeys();
        }
        //sendNotification(bestuid);
    }
    private void sortuids()
    {

        for (i = 0; i < locations.length; i++)
        {
            int min=i;
            for(j=i+1;j<locations.length;j++)
            {
                if(locations[min]<=locations[j])
                {
                    min=min;
                }
                else
                {
                    min=j;
                }
            }
            float temp;
            String tempo;
            temp=locations[i];
            tempo=uids[i];
            locations[i]=locations[min];
            uids[i]=uids[min];
            locations[min]=temp;
            uids[min]=tempo;

        }
        if(kite==0)
        {
            kite=kite+1;

            senddata(uids[0]);
            Toast.makeText(getApplicationContext(),uids[0],Toast.LENGTH_SHORT).show();

            //sendNotification(uids[0]);
            Log.d("joy", String.valueOf(uids[0]));
        }
    }
    Marker toiletmarker;
    private void senddata(final String uid)
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Toilets").child(uid).child("l");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    List<Object> map=(List<Object>)dataSnapshot.getValue();
                    double lat=0;
                    double lon=0;
                    if(map.get(0)!=null)
                    {
                        lat= Double.parseDouble(map.get(0).toString());
                    }
                    if(map.get(0)!=null)
                    {
                        lon= Double.parseDouble(map.get(1).toString());
                    }
                    LatLng serviceLatLng = new LatLng(lat,lon);
                    if(toiletmarker!=null)
                    {
                        toiletmarker.remove();
                    }
                    toiletmarker=mMap.addMarker(new MarkerOptions().position(serviceLatLng).title(uid));
                    try {
                        Geocoder g = new Geocoder(MapsActivity.this);
                        List<Address> list = g.getFromLocation(serviceLatLng.latitude, serviceLatLng.longitude, 1);
                        Address address = list.get(0);
                        String data = "";
                        if (address.getPremises() != null) {
                            data=data+address.getPremises();
                            Toast.makeText(MapsActivity.this,"Location"+data,Toast.LENGTH_LONG).show();
                            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("NearestToilet");

                            Map nmap=new HashMap();

                            nmap.put("Address",data);
                            reference.setValue(nmap);


                        }
                        else if (address.getSubLocality()!=null)
                        {
                            data=data+address.getPremises();
                            Toast.makeText(MapsActivity.this,"Location"+data,Toast.LENGTH_LONG).show();
                            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("NearestToilet");

                            Map nmap=new HashMap();

                            nmap.put("Address",data);
                            reference.setValue(nmap);
                        }


                    }
                    catch (Exception l) {
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(getApplicationContext()!=null){
            mLastLocation = location;

            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());


            fetchkeys();



        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }



    @Override
    protected void onStop() {
        super.onStop();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }
}
