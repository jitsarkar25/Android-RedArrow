package redarrow.dotapk.jit.redarrow;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
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
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    LoginButton loginButton;
    EditText email,password;
    Button login;
    ProgressDialog progressDialog;
    TextView register;
    static Map<String,ArrayList<String>> hashMap;
    CallbackManager callbackManager;
    ProfileTracker profileTracker;
    AccessTokenTracker accessTokenTracker;
    String fb_name,fb_email,fb_token;

    private final int RC_SIGN_IN = 101;
    static GoogleApiClient mGoogleApiClient;

    FacebookCallback<LoginResult> facebookCallback=new FacebookCallback<LoginResult>() {


        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("fbname", "success");
            final AccessToken accessToken = loginResult.getAccessToken();
            Log.d("fbtoken",accessToken.toString());
            Log.d("fbtoken2",accessToken.getToken());
            Log.d("fbtoken3",accessToken.getUserId());
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.v("LoginActivity", response.toString());

                            // Application code
                            try {
                                String email = object.getString("email");

                                String name=object.getString("name");
                                fblogin(email,name,accessToken.getUserId(),"facebook");
                                Log.v("Email",email );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // 01/31/1980 format
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();

            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    accessToken.getUserId(),
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
            /* handle the result */
                            Log.d("Login",response.toString());
                        }
                    }
            ).executeAsync();

            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {
                String name = profile.getName();
                Log.d("fbname", name);
               // Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
            }

        }
        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

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
        hashMap= new HashMap<String,ArrayList<String>>();
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("language2.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = in.readLine()) != null) {
               //String[] word = line.split("-");
                String a=line.split("-")[1];
                String b=line.split("-")[2];
                String c=line.split("-")[3];
                ArrayList <String> demolist=new ArrayList<>();
                demolist.add(a);
                demolist.add(b);
                demolist.add(c);
                hashMap.put(line.split("-")[0], demolist);
                Log.d("dict value",a + "    " + b);
            }
           //Log.d("dict value",hashMap.get("logout"));
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        SharedPreferences sharedPreferences=getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        boolean b=sharedPreferences.getBoolean("islogin", false);

        if(b)
        {
            Log.d("Donor Share",sharedPreferences.getBoolean("isdonor", false)+"");
            if(sharedPreferences.getBoolean("isdonor", false))
            {
                startActivity(new Intent(getApplicationContext(), DonorProfileActivity.class));
                finish();
            }
            else
            {
                startActivity(new Intent(getApplicationContext(), HospitalProfile2.class));
                finish();
            }
        }
        String lng=sharedPreferences.getString("language", "not");
        if(lng.equals("not")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("language", "english");
            editor.putInt("langno",0);
            editor.commit();
        }


        SharedPreferences fcmpreferences = getSharedPreferences("Fcm_token", Context.MODE_PRIVATE);
        Log.d("fcmtoken",fcmpreferences.getString("fcmtoken",""));


        //Fb
        callbackManager=CallbackManager.Factory.create();
        loginButton=(LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email" ));
        loginButton.registerCallback(callbackManager, facebookCallback);
        accessTokenTracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
        profileTracker=new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    String name = currentProfile.getFirstName();
                    Log.d("fbname", name);
                    //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
                }
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        //gmail part
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("106549887679-sqggombv9p7gi3egs2ak2ol1r3glck8k.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.d("fail","Failed");
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("event","buttonclicked");
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Log.d("test","Hererc");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            Log.d("test", "Hererc2");
        }
        else
             callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("test","here3");
        Log.d("test", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.d("recheck", acct.getDisplayName());
            Log.d("recheck", acct.getEmail());
            Log.d("recheck", acct.getId());
            Log.d("recheck", acct.getIdToken());
            Log.d("recheck", acct.getPhotoUrl().toString());
            SharedPreferences sharedPreferences=getSharedPreferences("dp_google", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("dplink", acct.getPhotoUrl().toString());
            editor.commit();
            fblogin(acct.getEmail(), acct.getDisplayName(), acct.getId(), "google");

        } else {
            // Signed out, show unauthenticated UI.

        }
        signout();
    }
    public void signout() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        Log.d("logout", "Logout");
                        // [END_EXCLUDE]
                    }
                });
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
                                    SharedPreferences sharedPreferences=getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("access_token", token);
                                    editor.commit();
                                    if(token!=null)
                                        checkLogin(token);
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
                                    Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_SHORT).show();
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
                                String emails=email.getText().toString();
                                String pass=password.getText().toString();
                                params.put("grant_type","password");
                                params.put("client_id","2");
                                params.put("client_secret","DnyHpJOb5e3b9ATWM3Y49TrudpUWKH1t908y9Vdu");
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

    @Override
    protected void onResume() {
        super.onResume();
        Profile profile= Profile.getCurrentProfile();
        if(profile!=null)
        {  String name=profile.getFirstName();
          //  Toast .makeText(getApplicationContext(),name,Toast.LENGTH_LONG).show();
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
                    String activated = jsonObject.getString("activated");
                    Log.d("activated", activated);
                    if (activated.equals("false")) {
                        Toast.makeText(MainActivity.this, "You need to activate your account from your email", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                    String hospital = jsonObject.getString("hospital");
                    String donor = jsonObject.getString("donor");
                    if (hospital.equals("null") && !donor.equals("null")) {
                        SharedPreferences fcmpreferences = getSharedPreferences("Fcm_token", Context.MODE_PRIVATE);
                        String fcmtoken = fcmpreferences.getString("fcmtoken", " ");
                        if (!fcmtoken.equals(" "))
                            sendfcmtoken(tk, fcmtoken);
                        JSONObject jo = jsonObject.getJSONObject("donor");
                        String id = jo.getString("id");
                        String name = jo.getString("name");
                        String dob = jo.getString("dob");
                        String address = jo.getString("address");
                        String contact = jo.getString("contact_no");
                        String blood_type = jo.getString("blood_type");
                        String health_issues = jo.getString("health_issues");
                        String lat = jo.getString("map_lat");
                        String lon = jo.getString("map_lng");

                        Donor donor1 = new Donor(id, name, dob, address, contact, blood_type, health_issues, lat, lon);

                        SharedPreferences sharedPreferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("id", id);
                        editor.putString("name", name);
                        editor.putString("dob", dob);
                        editor.putString("address", address);
                        editor.putString("contact", contact);
                        editor.putString("blood", blood_type);
                        editor.putString("issues", health_issues);
                        editor.putString("lat", lat);
                        editor.putString("lng", lon);
                        editor.putBoolean("isdonor", true);
                        editor.putBoolean("islogin", true);
                        editor.commit();


                        Intent intent = new Intent(getApplicationContext(), DonorProfileActivity.class);
                        intent.putExtra("donorinfo", donor1);
                        intent.putExtra("token", tk);
                        startActivity(intent);
                        finish();
                       // Toast.makeText(getApplicationContext(), "donor", Toast.LENGTH_SHORT).show();

                    } else if (donor.equals("null") && !hospital.equals("null")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("hospital");
                        String name = jsonObject1.getString("name");
                        String reg_no = jsonObject1.getString("reg_no");
                        String address = jsonObject1.getString("address");
                        String contact = jsonObject1.getString("contact_no");
                        String lat = jsonObject1.getString("map_lat");
                        String lng = jsonObject1.getString("map_lng");
                        Hospital hospital1 = new Hospital(name, reg_no, address, contact, lat, lng);
                        SharedPreferences sharedPreferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("name", name);
                        editor.putString("reg_no", reg_no);
                        editor.putString("address", address);
                        editor.putString("contact", contact);
                        editor.putString("lat", lat);
                        editor.putString("lng", lng);
                        editor.putBoolean("isdonor", false);
                        editor.putBoolean("islogin", true);
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), HospitalProfile2.class);
                        intent.putExtra("hospitalinfo", hospital1);
                        startActivity(intent);
                        finish();
                       // Toast.makeText(getApplicationContext(), "hospital", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("fbcheck", "here");
                        Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
                        intent.putExtra("name", fb_name);
                        intent.putExtra("email", fb_email);
                        intent.putExtra("token", tk);
                        startActivity(intent);

                    }

                    Log.d("Red ", "Donor " + donor + " Hospital " + hospital);
                   /* if(hospital == null)
                         Log.d("Red","type  donor");
                    String donor=jsonObject.getString("donor");
                    if(donor == null)
                        Log.d("Red","type  hospital");*/
                }
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

        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void sendfcmtoken(final String access_token,String fcm_token)
    {
        String url = "http://red-arrow.herokuapp.com/api/fcm/register/"+fcm_token;
        Log.d("fcm","url : "+url);

        StringRequest stringRequest=new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //    progressDialog.dismiss();
                Log.d("Red Arrow 2",response);
                Boolean suc=false;
                while(!suc) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        if (success.equals("true")) {
                            suc=true;
                            Log.d("fcm", "true");

                        } else {
                            Log.d("fcm", "fail");

                        }
                        // Log.d("Red ","Donor "+ donor + " Hospital "+hospital);
                   /* if(hospital == null)
                         Log.d("Red","type  donor");
                    String donor=jsonObject.getString("donor");
                    if(donor == null)
                        Log.d("Red","type  hospital");*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                headers.put("Authorization", "Bearer " + access_token);
                return headers;
            }
            {

            }


        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }



    public void fblogin(String email,String name,String id,String social)

    {
        Log.d("recheck",social);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please Wait!");
        progressDialog.show();
        final String emailfb=email;
        fb_email=email;
        fb_name=name;
        final String idfb=id;
        final String namefb=name;
        final String socialfb=name;
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
                    SharedPreferences sharedPreferences=getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("access_token", token);
                    editor.commit();
                    if(token!=null)
                        checkLogin(token);
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
                    Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_SHORT).show();
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
                params.put("grant_type","social");
                params.put("client_id","2");
                params.put("client_secret","DnyHpJOb5e3b9ATWM3Y49TrudpUWKH1t908y9Vdu");
                params.put("provider",socialfb);
                params.put("provider_user_id", idfb);
                params.put("name", namefb);
                params.put("email", emailfb);

                return params;
            }

        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
}
