package redarrow.dotapk.jit.redarrow;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditUser extends AppCompatActivity {
Donor donor;
    EditText name1,contact1,dob1,health1;
    String id,token;
    Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
       /* SharedPreferences sharedPreferences=getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
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
        */

        donor=(Donor)getIntent().getSerializableExtra("Donor");
        id=donor.getId();
        name1=(EditText)findViewById(R.id.EditName);
        String name=donor.getName();
        if(name.length()>21)
        {
            int l=name.lastIndexOf(' ');
            int u=name.indexOf(' ');
            String temp="";
            temp=temp+Character.toUpperCase(name.charAt(0));
            for(int i=u;i<l;i++)
            {
                if(name.charAt(i)==' ')
                    temp=temp+"."+Character.toUpperCase(name.charAt(i+1));
            }
            temp=temp+"."+name.substring(l+1);
            name=temp;
            donor.setName(name);
        }
        name1.setText(name);
        contact1=(EditText)findViewById(R.id.contact);
        contact1.setText(donor.getContact_no());
        dob1=(EditText)findViewById(R.id.dob);
        dob1.setText(donor.getDob());
        health1=(EditText)findViewById(R.id.health);
        health1.setText(donor.getHealth_issues());
       // token = getIntent().getStringExtra("token");

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

        dob1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditUser.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }
    public void updateInfo(View view)
    {
        SharedPreferences sharedPreferences=getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        final String token=sharedPreferences.getString("access_token", "");
        String url = "http://red-arrow.herokuapp.com/api/donor/"+id;
        StringRequest stringRequest1=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               // Log.d("update response",response);
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                    String t = jsonObject.getString("success");
                    if(t.contains("true"))
                    {
                        Toast.makeText(getApplicationContext(),"Updated SuccessFully",Toast.LENGTH_SHORT).show();
                        donor.setContact_no(contact1.getText().toString());
                        donor.setName(name1.getText().toString());
                        donor.setDob(dob1.getText().toString());
                        donor.setHealth_issues(health1.getText().toString());
                        SharedPreferences sharedPreferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("name",name1.getText().toString());
                        editor.putString("contact", contact1.getText().toString());
                        editor.putString("issues",health1.getText().toString());
                        editor.putString("dob",dob1.getText().toString());
                        editor.commit();

                        Intent intent=new Intent(getApplicationContext(), DonorProfileActivity.class);

                        startActivity(intent);
                        finish();
                    }
                    else if(t.contains("false"))
                    {
                        Toast.makeText(getApplicationContext(),"Couldn't Update:(",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof AuthFailureError)
                {
                    Toast.makeText(getApplicationContext(),"Auth Error",Toast.LENGTH_SHORT).show();
                }

                else if(error instanceof ServerError)
                {
                    Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_SHORT).show();
                }
                else if(error instanceof NoConnectionError)
                {
                    Toast.makeText(getApplicationContext(),"Connection Error",Toast.LENGTH_SHORT).show();
                }
                else if(error instanceof ParseError)
                {
                    Toast.makeText(getApplicationContext(),"Parse Error",Toast.LENGTH_SHORT).show();
                }
                else if(error instanceof TimeoutError)
                {
                    Toast.makeText(getApplicationContext(),"TimeOut Error",Toast.LENGTH_SHORT).show();
                }
                else if(error instanceof NetworkError)
                {
                    Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Unexpected Error",Toast.LENGTH_SHORT).show();
                }



            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name1.getText().toString());
                params.put("dob", donor.getDob());
                params.put("address", donor.getAddress());
                params.put("contact_no", contact1.getText().toString());
                params.put("blood_type", donor.getBlood_type());
                params.put("health_issues",health1.getText().toString());
                params.put("map_lat", donor.getLat());
                params.put("map_lng", donor.getLon());
                params.put("_method","PUT");
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map <String,String> headers =new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
            {

            }


        };
      //  Toast.makeText(getApplicationContext(),url,Toast.LENGTH_SHORT).show();

        MySingleton.getInstance(this).addToRequestQueue(stringRequest1);

    }
    public void updateAddress(View view)
    {
        Intent intent = new Intent(getApplicationContext(), DonorLocationSet.class);
        startActivityForResult(intent, 5);

       /* String token = getIntent().getStringExtra("token");
        Intent intent1=new Intent(getApplicationContext(),EditLocation.class);
        intent1.putExtra("donorinfo",donor);
        intent1.putExtra("token",token);
        startActivity(intent1);
        finish();*/
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==5)
        {
            if (resultCode== RESULT_OK) {
                donor.setAddress(data.getStringExtra("address"));
                // Toast.makeText(getApplicationContext(),data.getStringExtra("address"),Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("address",data.getStringExtra("address"));
                editor.putString("lat",data.getStringExtra("lat"));
                editor.putString("lng",data.getStringExtra("lon"));
                editor.commit();

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dob1.setText(sdf.format(myCalendar.getTime()));
    }
    }


