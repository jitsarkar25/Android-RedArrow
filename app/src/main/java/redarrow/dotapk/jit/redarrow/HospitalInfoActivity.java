package redarrow.dotapk.jit.redarrow;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HospitalInfoActivity extends AppCompatActivity {
    EditText regno,lochos,phonehos;
    Calendar myCalendar = Calendar.getInstance();
    String address,gotlat,gotlon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_info);
        regno = (EditText) findViewById(R.id.etRegno);
        lochos = (EditText) findViewById(R.id.etAddressHospital);
        phonehos = (EditText) findViewById(R.id.etPhoneHospital);



        lochos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HospitalLocationSet.class);
                startActivityForResult(intent, 5);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==5)
        {
            if (resultCode== RESULT_OK) {
                address=data.getStringExtra("address");
                // Toast.makeText(getApplicationContext(),data.getStringExtra("address"),Toast.LENGTH_SHORT).show();
                lochos.setText(data.getStringExtra("address"));
                gotlat= data.getStringExtra("lat");
                gotlon= data.getStringExtra("lon");
                //Log.d("Val", data.getStringExtra("lat") + "   " + data.getStringExtra("lon"));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void createhospital(View v) {
        if (lochos.getText().toString().equals("") || regno.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter proper Details", Toast.LENGTH_SHORT).show();
        } else {
            String url = "http://red-arrow.herokuapp.com/api/hospital";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    //  progressDialog.dismiss();
                    Log.d("Red Arrow", response);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        String token = jsonObject.getString("success");
                        //Log.d("create donor", token);
                        if(token.equals("true"))
                        {
                            Intent intent = new Intent(getApplicationContext(), HospitalProfile2.class);
                            Hospital hospital1=new Hospital( getIntent().getStringExtra("name"),regno.getText().toString(),address,getIntent().getStringExtra("contact"),gotlat,gotlon);
                            SharedPreferences sharedPreferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("name",getIntent().getStringExtra("name"));
                            editor.putString("reg_no",regno.getText().toString());
                            editor.putString("address",address);
                            editor.putString("contact",getIntent().getStringExtra("contact"));
                            editor.putString("lat",gotlat);
                            editor.putString("lng",gotlon);
                            editor.putBoolean("islogin",true);
                            editor.putBoolean("isdonor",false);
                            editor.commit();
                            intent.putExtra("hospitalinfo",hospital1);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

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
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    String name = getIntent().getStringExtra("name");
                    //String dobs = dob.getText().toString();
                    String contact = phonehos.getText().toString();
                    String reg = regno.getText().toString();
                    //if(health.equals(""))
                      //  health="none";
                    //String blood_type = bloodtypeselect.getSelectedItem().toString();
                    params.put("name", name);
                    params.put("address", address);
                    params.put("contact_no", contact);
                    params.put("reg_no", reg);
                    params.put("map_lat", gotlat);
                    params.put("map_lng", gotlon);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String token = getIntent().getStringExtra("token");
                    headers.put("Authorization", "Bearer " + token);
                    //   Log.d("Red","login "+headers.get("Authorization"));
                    return headers;
                }
            };

            MySingleton.getInstance(this).addToRequestQueue(stringRequest);
        }
    }
}
