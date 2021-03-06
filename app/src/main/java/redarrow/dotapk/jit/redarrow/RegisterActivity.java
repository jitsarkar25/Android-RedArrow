package redarrow.dotapk.jit.redarrow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    Button register;
    ProgressDialog progressDialog;
    EditText name,email,password,confpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        register=(Button)findViewById(R.id.bRegister);
        name=(EditText)findViewById(R.id.etName);
        email=(EditText)findViewById(R.id.etEmail);

        password=(EditText)findViewById(R.id.etPasswordReg);
        confpassword=(EditText)findViewById(R.id.etConfirmPassword);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bRegister:

                final String nam,ln,em,con,pass,conpass;
                nam=name.getText().toString();
                em=email.getText().toString();
                pass=password.getText().toString();
                conpass=confpassword.getText().toString();
                if(nam.equals("") || em.equals("")  || pass.equals("") || conpass.equals(""))
                {
                    Toast.makeText(this, "Enter the proper details", Toast.LENGTH_SHORT).show();

                }
                else if(pass.equals(conpass)== false)
                {
                    Toast.makeText(this, "Password doesnot match", Toast.LENGTH_SHORT).show();
                }
                else if(pass.length()<6)
                {
                    password.setError("Password needs to be atleast 6 Characters");
                    Toast.makeText(this, "Password needs to be atleast 6 Characters", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog=new ProgressDialog(this);
                    progressDialog.setTitle("Processing");
                    progressDialog.setMessage("Please Wait!");
                    progressDialog.show();
                    String url = "http://red-arrow.herokuapp.com/oauth/token";
                    StringRequest stringRequest=new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                           // progressDialog.dismiss();
                            progressDialog.dismiss();
                            Log.d("Register", response);
                            JSONObject jsonObject= null;
                            try {
                                jsonObject = new JSONObject(response);
                                String token=jsonObject.getString("access_token");
                                Log.d("Register Token", token);
                                Intent intent=new Intent(getApplicationContext(),ChooseActivity.class);
                                intent.putExtra("name",nam);
                                intent.putExtra("email",em);

                                intent.putExtra("token",token);
                                startActivity(intent);

                                finish();
                               /* if(token!=null)
                                    checkLogin(token);*/
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Log.d("Red", "Timeout");
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(getApplicationContext(),"Email Already Registered",Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ServerError) {
                                Log.d("Red", "Server Error");
                            } else if (error instanceof NetworkError) {
                                Log.d("Red", "Network Error");
                            } else if (error instanceof ParseError) {
                                Log.d("Red", "Parse Error");
                            }
                            progressDialog.dismiss();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map <String,String> params =new HashMap<>();
                            params.put("grant_type","register");
                            params.put("client_id","2");
                            params.put("client_secret","DnyHpJOb5e3b9ATWM3Y49TrudpUWKH1t908y9Vdu");
                            params.put("name",nam);
                            params.put("password", pass);
                            params.put("email", em);
                            params.put("scope", "*");
                            Log.d("Name",nam+pass+em);
                            return params;
                        }
                    };

                    MySingleton.getInstance(this).addToRequestQueue(stringRequest);


                }
                }
                 /*   startActivity(new Intent(getApplicationContext(),ChooseActivity.class));
                break;*/
        }

}
