package redarrow.dotapk.jit.redarrow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Viper PC on 2/25/2017.
 */
public class ApprovedAppointmentsAdapter extends ArrayAdapter<Notific> {

    Context con;
    public ApprovedAppointmentsAdapter(Context context, List<Notific> notifics) {
        super(context,R.layout.approvedappointment, notifics);
        con=context;
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View view = layoutInflater.inflate(R.layout.approvedappointment, parent, false);

        final Notific notific = getItem(position);
        TextView Doname = (TextView) view.findViewById(R.id.ApprovedAppointmentName);
        TextView Doadd = (TextView) view.findViewById(R.id.ApprovedAppointmentDonorAddress);
        final Button review = (Button) view.findViewById(R.id.ApprovedAppointmentReview);
        final TextView readReview=(TextView)view.findViewById(R.id.ApprovedAppointmentReviewRead);
        final EditText writeReview=(EditText)view.findViewById(R.id.ApprovedAppointmentReviewWrite);
        SharedPreferences preferences = con.getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        final int langno = preferences.getInt("langno", 0);
        review.setText(MainActivity.hashMap.get("read review").get(langno));




        Doname.setText(notific.getDonorname());
        Doadd.setText(notific.getDonor_address());
        readReview.setText(notific.getReview());

        if(notific.getReview()!=null && notific.getReview()!="null")
        {
            review.setText(MainActivity.hashMap.get("read review").get(langno));

        }
        else
        {
            review.setText(MainActivity.hashMap.get("write review").get(langno));
        }


        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(review.getText().toString().equals(MainActivity.hashMap.get("read review").get(langno)))
                {
                    readReview.setVisibility(View.VISIBLE);
                    review.setText(MainActivity.hashMap.get("close review").get(langno));
                }
                else if(review.getText().toString().equals(MainActivity.hashMap.get("close review").get(langno)))
                {
                    readReview.setVisibility(View.GONE);
                    review.setText(MainActivity.hashMap.get("read review").get(langno));
                }
                else if(review.getText().toString().equals(MainActivity.hashMap.get("write review").get(langno)))
                {
                    writeReview.setVisibility(View.VISIBLE);
                    final View vv=v;
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                    alertbox.setTitle(MainActivity.hashMap.get("enter review").get(langno));
                    final EditText input = new EditText(con);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    input.setTextColor(Color.BLACK);
                    alertbox.setView(input);
                    alertbox.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0,
                                                    int arg1) {

                                    if (!input.getText().toString().equals("") && input != null) {
                                        String url = "https://red-arrow.herokuapp.com/api/appointment/" + notific.getId() + "/review";
                                       // Log.d("urlapprove", url);
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                                            @Override
                                            public void onResponse(String response) {
                                                //    progressDialog.dismiss();
                                                //Log.d("Red Arrow 2",response);
                                              //  Log.d("url", response);
                                                JSONObject jsonObject = null;
                                                if (response != null) {
                                                    Toast.makeText(con, MainActivity.hashMap.get("review submitted").get(langno), Toast.LENGTH_SHORT).show();
                                                    notific.setReview(input.getText().toString());
                                                    review.setText(MainActivity.hashMap.get("read review").get(langno));
                                                    readReview.setText(input.getText().toString());
                                                    writeReview.setVisibility(View.GONE);
                                                    notifyDataSetChanged();
                                                    //   progressDialog.dismiss();
                                                }


                                            }

                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                //progressDialog.dismiss();
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

                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("review", input.getText().toString());
                                                return params;
                                            }


                                        };

                                        MySingleton.getInstance(con).addToRequestQueue(stringRequest);
                                        StartSmartAnimation.startAnimation(view, AnimationType.SlideOutUp, 1000, 0, true);

                                        //   Log.d("alert","clicked "+input.getText().toString());
                                    } else {
                                       // Toast.makeText(con, "Enter a review", Toast.LENGTH_SHORT).show();
                                        review.setText(MainActivity.hashMap.get("write review").get(langno));
                                        writeReview.setVisibility(View.GONE);
                                        //progressDialog.dismiss();
                                    }
                                }

                            });


                    alertbox.show();


                }

            }
        });

        return view;
    }
}
