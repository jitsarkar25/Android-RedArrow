package redarrow.dotapk.jit.redarrow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class DonorLocationSearch extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    HashMap <String,ArrayList<Donor>> bloodmap;
    Hospital hospital;
    Button search;
    Spinner bloodtypeselect,radiusselect;

    String selectblood,selectradius;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_location_search);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        bloodtypeselect=(Spinner)findViewById(R.id.spBloodType);
        radiusselect=(Spinner)findViewById(R.id.spRadius);
        search=(Button)findViewById(R.id.bSearch);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.blood_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodtypeselect.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.radius, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        radiusselect.setAdapter(adapter1);

        SharedPreferences sharedPreferences=getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        String defbldname=sharedPreferences.getString("defbloodname", "aaassa");
        if(!defbldname.equals("aaassa"))
        {
            bloodtypeselect.setSelection(sharedPreferences.getInt("defbloodpos",0)+1);
            radiusselect.setSelection(sharedPreferences.getInt("defradiuspos",0)+1);

        }
        SharedPreferences preferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        int langno=preferences.getInt("langno", 0);
        search.setText(MainActivity.hashMap.get("search").get(langno));


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectblood = bloodtypeselect.getSelectedItem().toString();
                selectradius = radiusselect.getSelectedItem().toString();
                if (selectblood.equals("Blood Type")) {
                    Toast.makeText(getApplicationContext(), "Enter Blood Type", Toast.LENGTH_SHORT).show();
                } else {
                    if (selectradius.equals("Radius")) {
                        Toast.makeText(getApplicationContext(), "Enter Radius", Toast.LENGTH_SHORT).show();
                    } else {
                        bloodmap = (HashMap<String, ArrayList<Donor>>) getIntent().getSerializableExtra("map");
                        hospital = (Hospital) getIntent().getSerializableExtra("hospitalinfo");
                        Double hospitallat = Double.parseDouble(hospital.getLat());
                        Double hospitallng = Double.parseDouble(hospital.getLng());
                        LatLng hospitallatLng = new LatLng(hospitallat, hospitallng);
                     /*   linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                           public void onGlobalLayout() {*/
                                mMap.clear();
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                                for (int i = 0; i < bloodmap.get(selectblood).size(); i++) {
                                    Double lat = Double.parseDouble(bloodmap.get(selectblood).get(i).getLat());
                                    Double lng = Double.parseDouble(bloodmap.get(selectblood).get(i).getLon());
                                    LatLng latLng = new LatLng(lat, lng);

                                  /*  Location location=new Location("");
                                    location.setLatitude(lat);
                                    location.setLongitude(lng);
                                    Location location1=new Location("");
                                    location1.setLatitude(Double.parseDouble(hospital.getLat()));
                                    location1.setLongitude(Double.parseDouble(hospital.getLng()));*/
                                    String dist[]=selectradius.split(" ");
                                    Double d= SphericalUtil.computeDistanceBetween(latLng, hospitallatLng);
                                    Double allowedDist=Double.parseDouble(dist[0])*1000;
                                    Log.d("Distance", "hospital and " + bloodmap.get(selectblood).get(i).getName() + " is " + d);
                                    if(d<=allowedDist) {
                                        builder.include(latLng);
                                        final int j=i;
                                        Marker marker;
                                        marker=mMap.addMarker(new MarkerOptions().position(latLng).title(String.valueOf(i)));
                                       mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                           @Override
                                           public boolean onMarkerClick(Marker marker) {
                                               Intent intent=new Intent(getApplicationContext(),DonorDetailsActivity.class);
                                               int no=Integer.parseInt(marker.getTitle());
                                               intent.putExtra("donorObj", bloodmap.get(selectblood).get(no));
                                               startActivity(intent);

                                               return false;
                                           }
                                       });

                                    }
                                    // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                }

                                builder.include(new LatLng(hospitallat,hospitallng));
                                mMap.addMarker(new MarkerOptions().position(hospitallatLng).title(hospital.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                LatLngBounds bounds = builder.build();
                                int padding = 150;
                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                                mMap.animateCamera(cu);
                            }
                       // });
                    }

                String rad = radiusselect.getSelectedItem().toString();
                Log.d("Spinner ", selectblood + rad);

            }
        });

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = AbsoluteLayout.LayoutParams.FILL_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);


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
        hospital = (Hospital) getIntent().getSerializableExtra("hospitalinfo");
        Double hospitallat = Double.parseDouble(hospital.getLat());
        Double hospitallng = Double.parseDouble(hospital.getLng());
        LatLng hospitallatLng = new LatLng(hospitallat, hospitallng);
        mMap.addMarker(new MarkerOptions().position(hospitallatLng).title(hospital.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hospitallatLng,15));
        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
