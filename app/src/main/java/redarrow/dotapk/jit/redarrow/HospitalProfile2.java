package redarrow.dotapk.jit.redarrow;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HospitalProfile2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        Hospital hospital;
        TextView hospnam,hospnamenav;
        ProgressDialog progressDialog;
        static HashMap<String,ArrayList<Donor>> bloodtypemap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_profile2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // toolbar.setLogo(R.drawable.nameaction);
        setSupportActionBar(toolbar);
        bloodtypemap=new HashMap<>();
        hospnam=(TextView)findViewById(R.id.etHospitalname);

// panel won't be null

       // hospnamenav=(TextView)findViewById(R.id.etHospitalnameNav);
        hospital= (Hospital) getIntent().getSerializableExtra("hospitalinfo");
        hospnam.setText(hospital.getName());
    //
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout =navigationView.getHeaderView(0);
        hospnamenav = (TextView)headerLayout.findViewById(R.id.etHospitalnameNav);
        hospnamenav.setText(hospital.getName());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hospital_profile2, menu);
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.finddonornav) {
            finddonorhos(new View(getApplicationContext()));
            // Handle the camera action
        } else if (id == R.id.notificationsnav) {

        } else if (id == R.id.reviewnav) {

        } else if (id == R.id.donationhistorynav) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.logoutnav) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void finddonorhos(View v)
    {
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please Wait!");
        progressDialog.show();
        String url = "http://red-arrow.herokuapp.com/api/donor";
        StringRequest stringRequest=new StringRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("Red Arrow ", response);
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);
                    int count=Integer.parseInt(jsonObject.getString("count"));
                    Donor donor[]=new Donor[count];
                    JSONArray jsonArray=jsonObject.getJSONArray("donors");
                    for(int i=0;i<count;i++)
                    {
                        JSONObject jo=jsonArray.getJSONObject(i);
                        String id=jo.getString("id");
                        String name=jo.getString("name");
                        String dob=jo.getString("dob");
                        String address=jo.getString("address");
                        String contact=jo.getString("contact_no");
                        String blood_type=jo.getString("blood_type");
                        String health_issues=jo.getString("health_issues");
                        String lat=jo.getString("map_lat");
                        String lon=jo.getString("map_lng");
                        donor[i]=new Donor(id,name,dob,address,contact,blood_type,health_issues,lat,lon);
                        if(bloodtypemap.containsKey(blood_type))
                        {
                            bloodtypemap.get(blood_type).add(donor[i]);
                        }
                        else
                        {
                            ArrayList<Donor> arrayList= new ArrayList<>();
                            arrayList.add(donor[i]);
                            bloodtypemap.put(blood_type,arrayList);
                        }

                    }
                    /*for(int i=0;i<bloodtypemap.get("A+").size();i++)
                     Log.d("Donor ",bloodtypemap.get("A+").get(i).getName());*/

                    // Log.d("Donor ", donor[26].getBlood_type());
                    Intent intent=new Intent(getApplicationContext(),DonorLocationSearch.class);
                    intent.putExtra("map",bloodtypemap);
                    intent.putExtra("hospitalinfo",hospital);
                    startActivity(intent);
                   /* String donor=jsonObject.getString("donor");
                    if(hospital.equals("null"))
                        Toast.makeText(getApplicationContext(), "donor", Toast.LENGTH_SHORT).show();
                    else {
                        startActivity(new Intent(getApplicationContext(),HospitalProfileActivity.class));
                        finish();
                        Toast.makeText(getApplicationContext(), "hospital", Toast.LENGTH_SHORT).show();

                    }*/
                    Log.d("Red ","count "+ count);
                   /* if(hospital == null)
                         Log.d("Red","type  donor");
                    String donor=jsonObject.getString("donor");
                    if(donor == null)
                        Log.d("Red","type  hospital");*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("Red","error");
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
