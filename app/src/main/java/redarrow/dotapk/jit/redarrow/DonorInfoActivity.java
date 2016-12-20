package redarrow.dotapk.jit.redarrow;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
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

public class DonorInfoActivity extends AppCompatActivity {
    EditText dob,loc,healthiisue;
    Calendar myCalendar = Calendar.getInstance();
    Spinner bloodtypeselect;
    String address,gotlat,gotlon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_info);
        dob = (EditText) findViewById(R.id.etDob);
        loc = (EditText) findViewById(R.id.etAddress);
        healthiisue = (EditText) findViewById(R.id.etHealthIssues);
        bloodtypeselect=(Spinner)findViewById(R.id.spBloodTypeSelect);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.blood_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodtypeselect.setAdapter(adapter);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(DonorInfoActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DonorLocationSet.class);
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
                loc.setText(data.getStringExtra("address"));
                gotlat= data.getStringExtra("lat");
                gotlon= data.getStringExtra("lon");
                //Log.d("Val", data.getStringExtra("lat") + "   " + data.getStringExtra("lon"));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dob.setText(sdf.format(myCalendar.getTime()));
    }

    public void createdonor(View v) {
        if (dob.getText().toString().equals("") || loc.getText().toString().equals("") || bloodtypeselect.getSelectedItem().toString().equals("Blood Type")) {
            Toast.makeText(getApplicationContext(), "Enter proper Details", Toast.LENGTH_SHORT).show();
        } else {
            String url = "http://red-arrow.herokuapp.com/api/donor";
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
                       Intent intent = new Intent(getApplicationContext(), DonorProfileActivity.class);
                       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       startActivity(intent);
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
                    String dobs = dob.getText().toString();
                    String contact = getIntent().getStringExtra("contact");
                    String health = healthiisue.getText().toString();
                    if(health.equals(""))
                        health="none";
                    String blood_type = bloodtypeselect.getSelectedItem().toString();
                    params.put("name", name);
                    params.put("dob", dobs);
                    params.put("address", address);
                    params.put("contact_no", contact);
                    params.put("blood_type", blood_type);
                    params.put("health_issues", health);
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
