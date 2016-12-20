package redarrow.dotapk.jit.redarrow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HospitalProfileActivity extends AppCompatActivity implements View.OnClickListener {
    Button search;
    static HashMap<String,ArrayList<Donor>> bloodtypemap;
    Hospital hospital;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bloodtypemap=new HashMap<>();
        setContentView(R.layout.activity_hospital_profile);
        search=(Button)findViewById(R.id.btSearchDonor);
        search.setOnClickListener(this);
        hospital= (Hospital) getIntent().getSerializableExtra("hospitalinfo");
        Log.d("Hospital",hospital.getName()+hospital.getLat()+hospital.getLng());
    }

    @Override
    public void onClick(View v) {
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
