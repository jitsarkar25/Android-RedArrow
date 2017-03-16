package redarrow.dotapk.jit.redarrow;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Viper PC on 2/25/2017.
 */
public class AcceptedAppointmentAdapter extends ArrayAdapter<Notific> {
    Context con;

    public AcceptedAppointmentAdapter(Context context, List<Notific> notifics) {
        super(context,R.layout.acceptedappointment, notifics);
        con=context;
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View view = layoutInflater.inflate(R.layout.acceptedappointment, parent, false);

        final Notific notific = getItem(position);
        TextView Dname = (TextView) view.findViewById(R.id.AcceptedAppointmentName);
        TextView Dadd = (TextView) view.findViewById(R.id.AcceptedAppointmentDonorAddress);
        Button approve = (Button) view.findViewById(R.id.AcceptedAppointmentApprove);
        SharedPreferences preferences = con.getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        final int langno = preferences.getInt("langno", 0);

        approve.setText(MainActivity.hashMap.get("approve").get(langno));

        Dname.setText(notific.getDonorname());
        Dadd.setText(notific.getDonor_address());


        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://red-arrow.herokuapp.com/api/appointment/" + notific.getId() + "/approve";
                Log.d("urlapprove",url);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //    progressDialog.dismiss();
                        //Log.d("Red Arrow 2",response);
                        Log.d("url", response);
                        JSONObject jsonObject= null;
                        if(response!=null)
                        {
                            Toast.makeText(con,MainActivity.hashMap.get("appointment approved").get(langno),Toast.LENGTH_SHORT).show();
                        }


                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // progressDialog.dismiss();
                        Toast.makeText(con, "error", Toast.LENGTH_SHORT).show();
                        // progressDialog.dismiss();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        SharedPreferences sharedPreferences = con.getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
                        String access = sharedPreferences.getString("access_token", "");
                        headers.put("Authorization", "Bearer " + access);
                        ///Log.d("Red","login "+headers.get("Authorization"));
                        return headers;
                    }




                };

                MySingleton.getInstance(con).addToRequestQueue(stringRequest);
                StartSmartAnimation.startAnimation(view,AnimationType.SlideOutUp,1000,0,true);
                final Handler handler = new Handler();
                handler.postDelayed(new

                                            Runnable() {
                                                @Override
                                                public void run() {
                                                    StartSmartAnimation.startAnimation(parent, AnimationType.SlideInUp, 1000, 0, true);
                                                    remove(getItem(position));
                                                    notifyDataSetChanged();
                                                }
                                            }

                        , 1000);

            }
        });

        return view;
    }
}
