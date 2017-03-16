package redarrow.dotapk.jit.redarrow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static redarrow.dotapk.jit.redarrow.R.drawable.border;
import static redarrow.dotapk.jit.redarrow.R.drawable.finddonorbeng;

public class DonorNotification extends AppCompatActivity {


        ArrayList<Notific> notification;
        ArrayList<Notific> acceptednotification;

        ProgressDialog progressDialog;
        LinearLayout linearLayout;
        TextView textView;
        ListView listView,listViewaccepted;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.donnotifi);

          /*  notification=(ArrayList<Notific>)getIntent().getSerializableExtra("notification");
            acceptednotification=(ArrayList<Notific>)getIntent().getSerializableExtra("acceptednotification");*/

            listView=(ListView)findViewById(R.id.listViewdonor);
         //   listViewaccepted=(ListView)findViewById(R.id.listViewAccepted);
            /*listViewaccepted.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);*/
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         //   Log.e("Button",notification.get(0).getHospital_name()+"+");

         /*   ListAdapter listAdapter = new DonorNotificationAdapter(this,notification);
            listView.setAdapter(listAdapter);

            ListAdapter listAdapter2 = new DonorAcceptedNotificationAdapter(this,acceptednotification);
            listViewaccepted.setAdapter(listAdapter2);*/

           /* received=(Button)findViewById(R.id.bReceivedNotifi);
            received.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Button", "received");
                    receivednotification();

                }
            });

            accepted=(Button)findViewById(R.id.bAcceptedNotifi);
            accepted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Button", "accepted");
                 acceptednorification();
                }
            });*/

            changelang();
try {
    if(getIntent().getIntExtra("val",0)==0)
         receivednotification();
    else
        acceptednorification();
}catch(Exception e)
{
    e.printStackTrace();
}


        }
    public void receivednotification()
    {
        SharedPreferences preferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        int langno = preferences.getInt("langno", 0);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle(MainActivity.hashMap.get("please wait").get(langno));
        progressDialog.setMessage(MainActivity.hashMap.get("fetching appointments").get(langno));
        progressDialog.show();
        String url = "http://red-arrow.herokuapp.com/api/notification";
        StringRequest stringRequest=new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //    progressDialog.dismiss();
                //Log.d("Red Arrow 2",response);

                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);
                    //int count=Integer.parseInt(jsonObject.getString("count"));
                    JSONArray jsonArray=jsonObject.getJSONArray("received");
                    int count= jsonArray.length();
                    // Notific notification[]=new Notific[count];
                    notification=new ArrayList<>();
                    for(int i=0;i<count;i++)
                    {
                        JSONObject jo=jsonArray.getJSONObject(i);
                        String id=jo.getString("id");
                        String hospital_id=jo.getString("hospital_id");
                        String time=jo.getString("created_at");
                        JSONObject hos=jo.getJSONObject("hospital");
                        String name= hos.getString("name");
                        String lat=hos.getString("map_lat");
                        String lng=hos.getString("map_lng");
                        String address=hos.getString("address");
                        notification.add(new Notific(id,hospital_id,time,name,lat,lng,address));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                   progressDialog.dismiss();
                }
                try {
                    ListAdapter listAdapter = new DonorNotificationAdapter(getApplicationContext(), notification);
                    listView.setAdapter(listAdapter);
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

    public void acceptednorification()
    {
        SharedPreferences preferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        int langno = preferences.getInt("langno", 0);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle(MainActivity.hashMap.get("please wait").get(langno));
        progressDialog.setMessage(MainActivity.hashMap.get("fetching appointments").get(langno));
        progressDialog.show();
        String url2 = "https://red-arrow.herokuapp.com/api/appointment/accepted";
        StringRequest stringRequest2=new StringRequest(Request.Method.GET,url2, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //    progressDialog.dismiss();
                //Log.d("Red Arrow 2",response);

                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);
                    //int count=Integer.parseInt(jsonObject.getString("count"));
                    JSONArray jsonArray=jsonObject.getJSONArray("accepted");
                    int count= jsonArray.length();
                    // Notific notification[]=new Notific[count];
                    acceptednotification=new ArrayList<>();
                    for(int i=0;i<count;i++)
                    {
                        JSONObject jo=jsonArray.getJSONObject(i);
                        String id=jo.getString("id");
                        String hospital_id=jo.getString("hospital_id");
                        String time=jo.getString("created_at");
                        JSONObject hos=jo.getJSONObject("hospital");
                        String name= hos.getString("name");
                        String lat=hos.getString("map_lat");
                        String lng=hos.getString("map_lng");
                        String address=hos.getString("address");
                        acceptednotification.add(new Notific(id,hospital_id,time,name,lat,lng,address));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
                ListAdapter listAdapter2 = new DonorAcceptedNotificationAdapter(getApplicationContext(),acceptednotification);
                listView.setAdapter(listAdapter2);
                progressDialog.dismiss();


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

        MySingleton.getInstance(this).addToRequestQueue(stringRequest2);


    }
    public void changelang() {
        SharedPreferences preferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        int langno = preferences.getInt("langno", 0);
     /*   received.setText(MainActivity.hashMap.get("received").get(langno));
        accepted.setText(MainActivity.hashMap.get("accepted").get(langno));*/
    }

}
