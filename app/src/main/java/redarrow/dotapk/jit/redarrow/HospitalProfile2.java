package redarrow.dotapk.jit.redarrow;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HospitalProfile2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        Hospital hospital;
        TextView hospnam,hospnamenav;
        int finddonorpic []= new int[3];
        int noti []= new int[3];
        int donrev []= new int[3];
        int donhis []= new int[3];

        ProgressDialog progressDialog;
        int iss=0;
        Menu menu,navmenu;
        static HashMap<String,ArrayList<Donor>> bloodtypemap;

        ImageView finddonor,notifi,review,history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_profile2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // toolbar.setLogo(R.drawable.nameaction);
        setSupportActionBar(toolbar);

        finddonor=(ImageView)findViewById(R.id.ivFindDonor);
        notifi=(ImageView)findViewById(R.id.ivNotification);
        review=(ImageView)findViewById(R.id.ivDonorReview);
        history=(ImageView)findViewById(R.id.ivDonationHistory);

        finddonorpic[0]=R.drawable.finddonor;
        finddonorpic[1]=R.drawable.finddonorhindi;
        finddonorpic[2]=R.drawable.finddonorbeng;

        noti[0]=R.drawable.sentappointments;
        noti[1]=R.drawable.sentappointmentshind;
        noti[2]=R.drawable.sentappointmentsbeng;

        donrev[0]=R.drawable.approvedappointments;
        donrev[1]=R.drawable.approvedappointmentshind;
        donrev[2]=R.drawable.approvedappointmentsbeng;

        donhis[0]=R.drawable.acceptedappointments;
        donhis[1]=R.drawable.acceptedappointmentshind;
        donhis[2]=R.drawable.acceptedappointmentsbeng;


        bloodtypemap=new HashMap<>();
        hospnam=(TextView)findViewById(R.id.etHospitalname);
        Log.d("state","create");
        SharedPreferences sharedPreferences=getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        boolean b=sharedPreferences.getBoolean("isdonor", true);
        if(!b)
        {
            Log.d("Here","Here1");
            String name=sharedPreferences.getString("name", "");
            String reg_no=sharedPreferences.getString("reg_no", "");
            String address=sharedPreferences.getString("address", "");
            String contact=sharedPreferences.getString("contact", "");
            String lat=sharedPreferences.getString("lat", "");
            String lng=sharedPreferences.getString("lng", "");
            hospital=new Hospital(name,reg_no,address,contact,lat,lng);

        }
       // Log.d("boolean",String.valueOf(b));
       // hospnamenav=(TextView)findViewById(R.id.etHospitalnameNav);
        else {
            Log.d("Here","Here2");
            hospital = (Hospital) getIntent().getSerializableExtra("hospitalinfo");

        }
        hospnam.setText(hospital.getName());
    //
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navmenu=navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout =navigationView.getHeaderView(0);
        hospnamenav = (TextView)headerLayout.findViewById(R.id.etHospitalnameNav);
        hospnamenav.setText(hospital.getName());

        SharedPreferences preferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        int langno=preferences.getInt("langno", 0);
        finddonor.setImageResource(finddonorpic[langno]);
        notifi.setImageResource(noti[langno]);
        review.setImageResource(donrev[langno]);
        history.setImageResource(donhis[langno]);

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
        this.menu=menu;
        iss=1;
        Log.d("state","create menu");
        getMenuInflater().inflate(R.menu.hospital_profile2, menu);
       // MenuItem bedMenuItem = menu.findItem(R.id.language_settings);
        SharedPreferences preferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        String lan=preferences.getString("language", "");
        int langno=preferences.getInt("langno", 0);

        MenuItem menuItemLogout = menu.findItem(R.id.logout_settings);
        menuItemLogout.setTitle(MainActivity.hashMap.get("logout").get(langno));
        MenuItem menuItemDefault = menu.findItem(R.id.default_settings);
        menuItemDefault.setTitle(MainActivity.hashMap.get("default settings").get(langno));
        MenuItem bedMenuItem = menu.findItem(R.id.language_settings);
        bedMenuItem.setTitle(MainActivity.hashMap.get("language").get(langno));
        changelang(langno);


        return true;
    }

    private void changelang(int langno) {
        MenuItem menuItemFind = navmenu.findItem(R.id.finddonornav);
        menuItemFind.setTitle(MainActivity.hashMap.get("find donor").get(langno));
        MenuItem menuItemNotifi = navmenu.findItem(R.id.menusentappointments);
        menuItemNotifi.setTitle(MainActivity.hashMap.get("sent appointments").get(langno));
        MenuItem menuReview = navmenu.findItem(R.id.menuacceptedappointmnets);
        menuReview.setTitle(MainActivity.hashMap.get("accepted appointments").get(langno));
        MenuItem menuHistory = navmenu.findItem(R.id.menuapprovedappointments);
        menuHistory.setTitle(MainActivity.hashMap.get("approved appointments").get(langno));
        MenuItem menuLogoutNav = navmenu.findItem(R.id.logoutnav);
        menuLogoutNav.setTitle(MainActivity.hashMap.get("logout").get(langno));
        MenuItem menuItemLogout = menu.findItem(R.id.logout_settings);
        menuItemLogout.setTitle(MainActivity.hashMap.get("logout").get(langno));
        MenuItem menuItemDefault = menu.findItem(R.id.default_settings);
        menuItemDefault.setTitle(MainActivity.hashMap.get("default settings").get(langno));
        MenuItem bedMenuItem = menu.findItem(R.id.language_settings);
        bedMenuItem.setTitle(MainActivity.hashMap.get("language").get(langno));
      /*  */

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.default_settings) {
            startActivity(new Intent(this, setActivityHos.class));
            return true;
        }
        else if(id == R.id.logout_settings)
        {
            LoginManager.getInstance().logOut();
            SharedPreferences preferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else if(id == R.id.language_settings)
        {
          /*  SharedPreferences preferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
            String lan=preferences.getString("language", "");
            if(lan.equals("english"))
            {

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("language", "hindi");
                editor.putInt("langno", 1);
                changelang(2);


                editor.commit();
            }
            else if(lan.equals("hindi"))
            {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("language","english");

                editor.putInt("langno", 0);
                changelang(0);
                editor.commit();
            }*/
                startActivity(new Intent(getApplicationContext(),LnguageChooseActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("state","resume");
        if(iss==1) {
            SharedPreferences preferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
            int langno = preferences.getInt("langno", 0);

            changelang(langno);

            finddonor.setImageResource(finddonorpic[langno]);
            notifi.setImageResource(noti[langno]);
            review.setImageResource(donrev[langno]);
            history.setImageResource(donhis[langno]);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.finddonornav) {
            finddonorhos(new View(getApplicationContext()));
            // Handle the camera action
        } else if (id == R.id.menusentappointments) {
            sentappointments(new View(getApplicationContext()));

        } else if (id == R.id.menuacceptedappointmnets) {
            accepappointments(new View(getApplicationContext()));

        } else if (id == R.id.menuapprovedappointments) {
            approvedappointment(new View(getApplicationContext()));



        } else if (id == R.id.logoutnav) {
            LoginManager.getInstance().logOut();
            SharedPreferences preferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void finddonorhos(View v)
    {
        SharedPreferences preferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        int langno = preferences.getInt("langno", 0);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle(MainActivity.hashMap.get("processing").get(langno));
        progressDialog.setMessage(MainActivity.hashMap.get("please wait").get(langno));
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

                    Log.d("Donor ", donor[8].getBlood_type());
                    Log.d("finddonor","Here");
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

public void sentappointments(View v)
{
    startActivity(new Intent(getApplicationContext(), SentAppointments.class));
}
    public void accepappointments(View v)
    {
        startActivity(new Intent(getApplicationContext(),AcceptedAppointmentActivity.class));
    }

    public void approvedappointment(View v)
    {
        startActivity(new Intent(getApplicationContext(),ApprovedAppointmentsActivity.class));
    }
}
