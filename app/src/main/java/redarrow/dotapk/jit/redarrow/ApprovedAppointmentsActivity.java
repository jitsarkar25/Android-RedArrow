package redarrow.dotapk.jit.redarrow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
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

public class ApprovedAppointmentsActivity extends AppCompatActivity {

    ArrayList<Notific> notification;
    ListView approvedAppointment;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_appointments);
        approvedAppointment=(ListView)findViewById(R.id.lvApprovedAppointment);

        SharedPreferences preferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        int langno = preferences.getInt("langno", 0);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle(MainActivity.hashMap.get("please wait").get(langno));
        progressDialog.setMessage(MainActivity.hashMap.get("fetching appointments").get(langno));
        progressDialog.show();
        String url = "https://red-arrow.herokuapp.com/api/appointment/approved";
        StringRequest stringRequest=new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //    progressDialog.dismiss();
                //Log.d("Red Arrow 2",response);

                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);
                    //int count=Integer.parseInt(jsonObject.getString("count"));
                    JSONArray jsonArray=jsonObject.getJSONArray("approved");
                    int count= jsonArray.length();
                    // Notific notification[]=new Notific[count];
                    notification=new ArrayList<>();
                    for(int i=0;i<count;i++)
                    {
                        JSONObject jo=jsonArray.getJSONObject(i);
                        JSONObject hos=jo.getJSONObject("hospital");
                        String lat=hos.getString("map_lat");
                        String lng=hos.getString("map_lng");

                        JSONObject don=jo.getJSONObject("donor");
                        String donlat=don.getString("map_lat");
                        String donlng=don.getString("map_lng");
                        String donname=don.getString("name");
                        String donaddress=don.getString("address");

                        Notific sendap=new Notific();
                        sendap.setDonorname(donname);
                        sendap.setDonor_map_lat(donlat);
                        sendap.setDonor_map_lng(donlng);
                        sendap.setLat(lat);
                        sendap.setLng(lng);
                        sendap.setDonor_address(donaddress);
                        sendap.setId(jo.getString("id"));
                        sendap.setReview(jo.getString("donor_review"));

                        notification.add(sendap);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
                try {
                    ListAdapter listAdapter = new ApprovedAppointmentsAdapter(getApplicationContext(), notification);
                    approvedAppointment.setAdapter(listAdapter);
                     progressDialog.dismiss();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                     progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                 progressDialog.dismiss();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map <String,String> headers =new HashMap<>();
                SharedPreferences sharedPreferences=getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
                String access=sharedPreferences.getString("access_token","");
                headers.put("Authorization", "Bearer " + access);
                ///Log.d("Red","login "+headers.get("Authorization"));
                return headers;
            }
            {

            }


        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
