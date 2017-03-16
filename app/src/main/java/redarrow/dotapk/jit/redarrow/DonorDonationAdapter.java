package redarrow.dotapk.jit.redarrow;

import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;

import java.util.List;
import java.util.Map;

/**
 * Created by Viper PC on 2/24/2017.
 */
public class DonorDonationAdapter extends ArrayAdapter<Notific> {
    Context con;

    public DonorDonationAdapter(Context context, List<Notific> notifics) {
        super(context,R.layout.donordonation, notifics);
        con=context;
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View view = layoutInflater.inflate(R.layout.donordonation, parent, false);

        final Notific notific = getItem(position);
        TextView Hname = (TextView) view.findViewById(R.id.donorHospitalName);
        TextView Hadd = (TextView) view.findViewById(R.id.donorHospitalAddress);
        final TextView Review = (TextView) view.findViewById(R.id.donorHospitalReview);
        final Button review = (Button) view.findViewById(R.id.donorReview);

        SharedPreferences preferences = con.getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        final int langno = preferences.getInt("langno", 0);
        review.setText(MainActivity.hashMap.get("read review").get(langno));
        Hname.setText(notific.getHospital_name());
        Hadd.setText(notific.getHospital_address());
        Review.setText("\""+notific.getReview()+"\"");

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Review.getVisibility() == View.GONE) {
                    Review.setVisibility(View.VISIBLE);
                    review.setText(MainActivity.hashMap.get("close review").get(langno));
                } else {
                    Review.setVisibility(View.GONE);
                    review.setText(MainActivity.hashMap.get("read review").get(langno));
                }


              /*  try {
                  /*  AlertDialog alertDialog;
                    alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Review");
                    alertDialog.setMessage(notific.getReview());
                    alertDialog.show();
                } catch (Exception e) {
                    Log.d("error", e.getMessage());
                }*/
            }
        });

        return view;
    }
}
