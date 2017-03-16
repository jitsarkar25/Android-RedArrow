package redarrow.dotapk.jit.redarrow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DonorDetailsActivity extends AppCompatActivity {

    TextView donorName, donorPh, donorHealth,headName,headPhone,headHealth;
    Button call,notification;
    Donor donor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_donor_details);
        donorName = (TextView) findViewById(R.id.tvDonorNameDet);
        donorPh = (TextView) findViewById(R.id.tvDonorContactDet);
        donorHealth = (TextView) findViewById(R.id.tvDonorHealthDet);
        headName = (TextView) findViewById(R.id.tvNamedet);
        headPhone = (TextView) findViewById(R.id.tvPhonedet);
        headHealth = (TextView) findViewById(R.id.tvHealthdet);

        call=(Button)findViewById(R.id.bcalldonor);
        notification=(Button)findViewById(R.id.brequestdonation);

        DisplayMetrics dm = new DisplayMetrics();

        Intent i = getIntent();
        donor = (Donor) i.getSerializableExtra("donorObj");
        donorName.setText(donor.getName());
        donorPh.setText(donor.getContact_no());
        donorHealth.setText(donor.getHealth_issues());

        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.8));
        SharedPreferences sharedPreferences=getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        int lango=sharedPreferences.getInt("langno",0);
        headName.setText(MainActivity.hashMap.get("name").get(lango));
        headPhone.setText(MainActivity.hashMap.get("phoneno").get(lango));
        headHealth.setText(MainActivity.hashMap.get("healthissues").get(lango));
        call.setText(MainActivity.hashMap.get("call").get(lango));
        notification.setText(MainActivity.hashMap.get("request").get(lango));

    }
    public void sendnotification(View v)
    {
        SharedPreferences sharedPreferences=getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        final String access_token=sharedPreferences.getString("access_token", "12345");
        final String donor_id=donor.getId();

        String url = "http://red-arrow.herokuapp.com/api/appointment";
        StringRequest stringRequest=new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            SharedPreferences preferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
            int langno=preferences.getInt("langno", 0);
            @Override
            public void onResponse(String response) {
                //progressDialog.dismiss();

                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);
                    String success=jsonObject.getString("success");
                    Log.d("success",success);
                    if(success.equals("true"))
                    {
                        Toast.makeText(getApplicationContext(),MainActivity.hashMap.get("notification sent").get(langno),Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Red", "error");
               // progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params =new HashMap<>();
              //  String emails=email.getText().toString();
                //String pass=password.getText().toString();
                params.put("donor_id",donor_id);
              /*  params.put("client_id","1");
                params.put("client_secret","VKtTn6AdLuXgSTb5A1vn2KH2z9yWufKrWdiWDpnj");
                params.put("username",emails);
                params.put("password", pass);
                params.put("scope", "*");*/
                return params;
            }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                Map <String,String> headers =new HashMap<>();
                headers.put("Authorization", "Bearer " + access_token);
                return headers;
            }


            };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);

        //  Log.d("donor_id",donor.getId());
    }

    public void calldonor(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + donorPh.getText().toString()));
        startActivity(intent);
    }
}
