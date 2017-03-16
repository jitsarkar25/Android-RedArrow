package redarrow.dotapk.jit.redarrow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Viper PC on 2/25/2017.
 */
public class SentAppointmentAdapter extends ArrayAdapter<Notific> {
    Context con;

    public SentAppointmentAdapter(Context context, List<Notific> notifics) {
        super(context,R.layout.sentappointments, notifics);
        con=context;
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View view = layoutInflater.inflate(R.layout.sentappointments, parent, false);

        final Notific notific = getItem(position);
        TextView DonorName = (TextView) view.findViewById(R.id.sentAppointmentDonorname);
        TextView DonorAdd = (TextView) view.findViewById(R.id.sentAppointmentDonorAddress);

         Button donordirection = (Button) view.findViewById(R.id.sentAppointmentDirection);
        SharedPreferences preferences = con.getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        int langno = preferences.getInt("langno", 0);
        donordirection.setText(MainActivity.hashMap.get("direction").get(langno));
         donordirection.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                         Uri.parse("http://maps.google.com/maps?daddr=" + notific.getDonor_map_lat() + "," + notific.getDonor_map_lng()));
                 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 con.startActivity(intent);
             }
         });

        DonorName.setText(notific.getDonorname());
        DonorAdd.setText(notific.getDonor_address());





        return view;
    }

}
