package redarrow.dotapk.jit.redarrow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DonorProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView donorname,bloodtype;
    ImageView details,received,accepted,donhist;
    Donor donor;
    String accesstoken;
    RoundedImageView dpPic;
    Menu menu,navmenu;
    int iss=0;
    int mydetails []= new int[3];
    int acceptnoti []= new int[3];
    int recdnoti []= new int[3];
    int donhis []= new int[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        donorname=(TextView)findViewById(R.id.tvDonorName);
        bloodtype=(TextView)findViewById(R.id.tvDonorBloodType);
        dpPic=(RoundedImageView)findViewById(R.id.ivDonorDp);

        details=(ImageView)findViewById(R.id.ivDonorDetails);
        received=(ImageView)findViewById(R.id.ivDonorNotification);
        accepted=(ImageView)findViewById(R.id.ivDonorLocation);
        donhist=(ImageView)findViewById(R.id.ivDonorHistory);

        SharedPreferences googleshared=getSharedPreferences("dp_google", Context.MODE_PRIVATE);
        String dpurl=googleshared.getString("dplink", "nope");

        mydetails[0]=R.drawable.detailss;
        mydetails[1]=R.drawable.mydetailshind;
        mydetails[2]=R.drawable.mydetailsbeng;

        acceptnoti[0]=R.drawable.acceptedappointments;
        acceptnoti[1]=R.drawable.acceptedappointmentshind;
        acceptnoti[2]=R.drawable.acceptedappointmentsbeng;

        recdnoti[0]=R.drawable.receivedappointments;
        recdnoti[1]=R.drawable.receivedappointmentshind;
        recdnoti[2]=R.drawable.receivedappointmentsbeng;

        donhis[0]=R.drawable.donationhistory;
        donhis[1]=R.drawable.donationhistoryhindi;
        donhis[2]=R.drawable.donationhistorybeng;

        if(!dpurl.equals("nope"))
            Picasso.with(getApplicationContext()).load(dpurl).into(dpPic);

        SharedPreferences sharedPreferences=getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        boolean b=sharedPreferences.getBoolean("isdonor", false);
        if(b)
        {
            String name=sharedPreferences.getString("name", "");
            String id=sharedPreferences.getString("id", "");
            String dob=sharedPreferences.getString("dob", "");
            String address=sharedPreferences.getString("address", "");
            String contact=sharedPreferences.getString("contact", "");
            String blood=sharedPreferences.getString("blood", "");
            String issues=sharedPreferences.getString("issues", "");
            String lat=sharedPreferences.getString("lat", "");
            String lng=sharedPreferences.getString("lng", "");

            donor=new Donor(id,name,dob,address,contact,blood,issues,lat,lng);
        }
        // Log.d("boolean",String.valueOf(b));
        // hospnamenav=(TextView)findViewById(R.id.etHospitalnameNav);
        else {
            donor = (Donor) getIntent().getSerializableExtra("donorinfo");
        }
        accesstoken=sharedPreferences.getString("access_token"," ");
        Log.d("access revoke",accesstoken);
        donorname.setText(donor.getName());
        bloodtype.setText(donor.getBlood_type());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navmenu=navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences preferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        int langno=preferences.getInt("langno", 0);
        Log.d("langnodonor",langno+"");
        details.setImageResource(mydetails[langno]);
        received.setImageResource(recdnoti[langno]);
        accepted.setImageResource(acceptnoti[langno]);
        donhist.setImageResource(donhis[langno]);


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
        this.menu=menu;
        iss=1;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.donor_profile, menu);

        SharedPreferences preferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        String lan=preferences.getString("language", "");
        int langno=preferences.getInt("langno", 0);
        MenuItem menuItemLogout = menu.findItem(R.id.donor_logout_settings);
        menuItemLogout.setTitle(MainActivity.hashMap.get("logout").get(langno));
        MenuItem bedMenuItem = menu.findItem(R.id.donor_language_settings);
        bedMenuItem.setTitle(MainActivity.hashMap.get("language").get(langno));
        changelang(langno);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.donor_language_settings) {
            startActivity(new Intent(getApplicationContext(),LnguageChooseActivity.class));
            return true;
        }
        if(id == R.id.donor_logout_settings)
        {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void changelang(int langno) {
        MenuItem menuItemFind = navmenu.findItem(R.id.editdetailsdonor);
        menuItemFind.setTitle(MainActivity.hashMap.get("my details").get(langno));
        MenuItem menuItemNotifi = navmenu.findItem(R.id.notificationsdonor);
        menuItemNotifi.setTitle(MainActivity.hashMap.get("received appointments").get(langno));
        MenuItem menuReview = navmenu.findItem(R.id.updatelocdonor);
        menuReview.setTitle(MainActivity.hashMap.get("accepted appointments").get(langno));
        MenuItem menuHistory = navmenu.findItem(R.id.donationhistorydonor);
        menuHistory.setTitle(MainActivity.hashMap.get("donation history").get(langno));

        MenuItem menuLogoutNav = navmenu.findItem(R.id.logoutnavDonor);
        menuLogoutNav.setTitle(MainActivity.hashMap.get("logout").get(langno));
        MenuItem menuItemLogout = menu.findItem(R.id.donor_logout_settings);
        menuItemLogout.setTitle(MainActivity.hashMap.get("logout").get(langno));
        MenuItem bedMenuItem = menu.findItem(R.id.donor_language_settings);
        bedMenuItem.setTitle(MainActivity.hashMap.get("language").get(langno));
        ((TextView)findViewById(R.id.donorBloodtype)).setText(MainActivity.hashMap.get("blood type").get(langno));
        ((TextView)findViewById(R.id.donorDonations)).setText(MainActivity.hashMap.get("donation").get(langno));
      /*  */

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("state","resume");
        if(iss==1) {
            SharedPreferences preferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
            int langno = preferences.getInt("langno", 0);

            changelang(langno);

            details.setImageResource(mydetails[langno]);
            received.setImageResource(recdnoti[langno]);
            accepted.setImageResource(acceptnoti[langno]);
            donhist.setImageResource(donhis[langno]);
         /*   finddonor.setImageResource(finddonorpic[langno]);
            notifi.setImageResource(noti[langno]);
            review.setImageResource(donrev[langno]);
            history.setImageResource(donhis[langno]);*/
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.editdetailsdonor) {
            mydetails(new View(getApplicationContext()));
            // Handle the camera action
        } else if (id == R.id.notificationsdonor) {
            myNotification(new View(getApplicationContext()));

        } else if (id == R.id.updatelocdonor) {
            donorreceivedappointments(new View(getApplicationContext()));

        } else if (id == R.id.donationhistorydonor) {
            donationhistory(new View(getApplicationContext()));
        }  else if (id == R.id.logoutnavDonor) {
           logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout()
    {
        SharedPreferences googleshared=getSharedPreferences("dp_google", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorg = googleshared.edit();
        editorg.clear();
        editorg.commit();

        LoginManager.getInstance().logOut();
        SharedPreferences fcmpreferences = getSharedPreferences("Fcm_token", Context.MODE_PRIVATE);
        String fcmtoken=fcmpreferences.getString("fcmtoken"," ");
        String url = "http://red-arrow.herokuapp.com/api/fcm/revoke/"+fcmtoken;
        Log.d("fcm revoke", "url : " + url);

        StringRequest stringRequest=new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //    progressDialog.dismiss();
                Log.d("Red Arrow 2",response);
                Boolean suc=false;
                while(!suc) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        if (success.equals("true")) {
                            suc=true;
                            Log.d("fcm", "true revoke");

                        } else {
                            Log.d("fcm", "fail revoke");

                        }
                        // Log.d("Red ","Donor "+ donor + " Hospital "+hospital);
                   /* if(hospital == null)
                         Log.d("Red","type  donor");
                    String donor=jsonObject.getString("donor");
                    if(donor == null)
                        Log.d("Red","type  hospital");*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressDialog.dismiss();
                Log.d("Red","error");
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map <String,String> headers =new HashMap<>();

                Log.d("access revoke",accesstoken);
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }
            {

            }


        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);

        SharedPreferences preferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void mydetails(View v)
    {
        Intent intent=new Intent(getApplicationContext(),MyDetails.class);
        intent.putExtra("Donor", donor);
        startActivity(intent);

    }

    public void myNotification(View v)
    {

        Intent intent=new Intent(getApplicationContext(),DonorNotification.class);
        intent.putExtra("val",0);
        startActivity(intent);
    }

    public void donationhistory(View v )

    {
        startActivity(new Intent(getApplicationContext(),DonorDonationHistoryActivity.class));

    }

    public void donorreceivedappointments(View v)
    {
        Intent intent=new Intent(getApplicationContext(),DonorNotification.class);
        intent.putExtra("val",1);
        startActivity(intent);

    }
}
