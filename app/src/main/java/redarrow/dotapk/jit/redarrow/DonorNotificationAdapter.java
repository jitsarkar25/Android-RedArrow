package redarrow.dotapk.jit.redarrow;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.logging.LogRecord;

/**
 * Created by jit on 31-01-2017.
 */
public class DonorNotificationAdapter extends ArrayAdapter<Notific> {
    Context con;
    Button accept,showdirection,remove;
    public DonorNotificationAdapter(Context context, List<Notific> notifics) {
        super(context,R.layout.donornotificationcontent, notifics);
        con=context;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        final View view=layoutInflater.inflate(R.layout.donornotificationcontent, parent, false);

        final Notific notific=getItem(position);
        TextView msg=(TextView)view.findViewById(R.id.notifiMessage);
        TextView date=(TextView)view.findViewById(R.id.notifiDate);
        TextView time=(TextView)view.findViewById(R.id.notifiTime);
        showdirection=(Button)view.findViewById(R.id.notifiDirection);
        showdirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + notific.getLat() + "," + notific.getLng()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                con.startActivity(intent);//তে আপনার রক্তের খুব দরকারमें आपका खून का ज़रूरत है
            }
        });
        SharedPreferences preferences = con.getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        int langno = preferences.getInt("langno", 0);
        if(langno==2)
            msg.setText(notific.getHospital_name()+" তে আপনার রক্তের খুব দরকার");
        else if(langno==1)
            msg.setText(notific.getHospital_name()+" में आपका खून का ज़रूरत है");
        else
            msg.setText("Your blood is urgently required at " + notific.getHospital_name());
        date.setText(notific.getCreated_at().split(" ")[0]);
        time.setText(notific.getCreated_at().split(" ")[1]);

        accept=(Button)view.findViewById(R.id.notifiAccept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://red-arrow.herokuapp.com/api/appointment/"+notific.getId()+"/accept";
                Log.d("url",url);
                StringRequest stringRequest=new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                    //    progressDialog.dismiss();
                        Log.d("url", response);
                        JSONObject jsonObject= null;
                        if(response!=null)
                        {
                           // Toast.makeText(con,"Awaiting Hospital's Confirmation",Toast.LENGTH_SHORT).show();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Log.d("Red", "Timeout");
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(con, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Log.d("Red", "Server Error");
                        } else if (error instanceof NetworkError) {
                            Log.d("Red", "Network Error");
                        } else if (error instanceof ParseError) {
                            Log.d("Red", "Parse Error");
                        }
                      //  progressDialog.dismiss();
                    }
                })
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map <String,String> headers =new HashMap<>();
                        SharedPreferences sharedPreferences=con.getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
                        String access=sharedPreferences.getString("access_token","");
                        headers.put("Authorization", "Bearer " + access);
                        Log.d("Red","login "+headers.get("Authorization"));
                        return headers;
                    }


                };

                MySingleton.getInstance(con).addToRequestQueue(stringRequest);
                StartSmartAnimation.startAnimation(view, AnimationType.SlideOutUp, 1000, 0, true);
                final Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        StartSmartAnimation.startAnimation(parent, AnimationType.SlideInUp, 1000, 0, true);
                        remove(getItem(position));
                        notifyDataSetChanged();
                    }
                }, 1000);

            }
        });

        remove=(Button)view.findViewById(R.id.notifiRemove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://red-arrow.herokuapp.com/api/appointment/"+notific.getId()+"/reject";
                Log.d("url",url);
                StringRequest stringRequest=new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //    progressDialog.dismiss();
                        Log.d("url", response);
                        JSONObject jsonObject= null;
                        if(response!=null)
                        {
                          //  Toast.makeText(con,"Notification removed",Toast.LENGTH_SHORT).show();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Log.d("Red", "Timeout");
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(con, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Log.d("Red", "Server Error");
                        } else if (error instanceof NetworkError) {
                            Log.d("Red", "Network Error");
                        } else if (error instanceof ParseError) {
                            Log.d("Red", "Parse Error");
                        }
                        //  progressDialog.dismiss();
                    }
                })
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map <String,String> headers =new HashMap<>();
                        SharedPreferences sharedPreferences=con.getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
                        String access=sharedPreferences.getString("access_token","");
                        headers.put("Authorization", "Bearer " + access);
                        Log.d("Red","login "+headers.get("Authorization"));
                        return headers;
                    }


                };

                MySingleton.getInstance(con).addToRequestQueue(stringRequest);
                StartSmartAnimation.startAnimation(view, AnimationType.SlideOutRight, 1000, 0, true);
                final Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        StartSmartAnimation.startAnimation(parent, AnimationType.SlideInUp, 1000, 0, true);
                        remove(getItem(position));
                        notifyDataSetChanged();
                    }
                },1000);

            }
        });

        changelang();
        return view;
    }
    public void changelang() {
        SharedPreferences preferences = con.getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        int langno = preferences.getInt("langno", 0);
        accept.setText(MainActivity.hashMap.get("accept").get(langno));
        remove.setText(MainActivity.hashMap.get("reject").get(langno));
        showdirection.setText(MainActivity.hashMap.get("direction").get(langno));
    }
}
