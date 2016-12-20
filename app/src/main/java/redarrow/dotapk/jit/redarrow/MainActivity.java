package redarrow.dotapk.jit.redarrow;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Hashtable;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText email,password;
    Button login;
    ProgressDialog progressDialog;
    TextView register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=(EditText)findViewById(R.id.etEmailAdd);
        password=(EditText)findViewById(R.id.etPassword);
        register=(TextView)findViewById(R.id.tvRegister);
        login=(Button)findViewById(R.id.bLogin);
        login.setOnClickListener(this);
        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.bLogin:
                progressDialog=new ProgressDialog(this);
                progressDialog.setTitle("Processing");
                progressDialog.setMessage("Please Wait!");
                progressDialog.show();
                String emails=email.getText().toString();
                String pass=password.getText().toString();
                if(emails.equals("") || pass.equals(""))
                {
                    Toast.makeText(this, "Enter the proper details", Toast.LENGTH_SHORT).show();

                }
                else {
                    if(isNetworkAvailable()) {

                        String url = "http://red-arrow.herokuapp.com/oauth/token";
                        StringRequest stringRequest=new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                Log.d("Red Arrow",response);
                                JSONObject jsonObject= null;
                                try {
                                    jsonObject = new JSONObject(response);
                                    String token=jsonObject.getString("access_token");
                                    if(token!=null)
                                        checkLogin(token);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }



                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Red","error");
                                progressDialog.dismiss();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map <String,String> params =new HashMap<>();
                                String emails=email.getText().toString();
                                String pass=password.getText().toString();
                                params.put("grant_type","password");
                                params.put("client_id","1");
                                params.put("client_secret","VKtTn6AdLuXgSTb5A1vn2KH2z9yWufKrWdiWDpnj");
                                params.put("username",emails);
                                params.put("password", pass);
                                params.put("scope", "*");
                                return params;
                            }

                        };

                        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


                    }
                    else
                    {
                        AlertDialog alertDialog;
                        alertDialog=new AlertDialog.Builder(this).create();
                        alertDialog.setTitle("Status");
                        alertDialog.setMessage("No Internet Connection");
                        alertDialog.show();
                    }
                }

                break;
            case R.id.tvRegister:
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
                break;
        }

    }
    private void checkLogin(String token)
    {
        final String tk=token;
        String url = "http://red-arrow.herokuapp.com/api/user";

        StringRequest stringRequest=new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
            //    progressDialog.dismiss();
                Log.d("Red Arrow 2",response);
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);
                    String hospital=jsonObject.getString("hospital");
                    String donor=jsonObject.getString("donor");
                    if(hospital.equals("null"))
                        Toast.makeText(getApplicationContext(),"donor",Toast.LENGTH_SHORT).show();
                    else {
                        JSONObject jsonObject1=jsonObject.getJSONObject("hospital");
                        String name=jsonObject1.getString("name");
                        String reg_no=jsonObject1.getString("reg_no");
                        String address=jsonObject1.getString("address");
                        String contact=jsonObject1.getString("contact_no");
                        String lat=jsonObject1.getString("map_lat");
                        String lng=jsonObject1.getString("map_lng");
                        Hospital hospital1=new Hospital(name,reg_no,address,contact,lat,lng);
                        Intent intent=new Intent(getApplicationContext(), HospitalProfile2.class);
                        intent.putExtra("hospitalinfo",hospital1);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), "hospital", Toast.LENGTH_SHORT).show();

                    }
                    Log.d("Red ","Donor "+ donor + " Hospital "+hospital);
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
               // progressDialog.dismiss();
                Log.d("Red","error");
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map <String,String> headers =new HashMap<>();
                headers.put("Authorization", "Bearer " + tk);
                Log.d("Red","login "+headers.get("Authorization"));
                return headers;
            }
            {

            }


        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
