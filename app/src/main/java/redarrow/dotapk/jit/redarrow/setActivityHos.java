package redarrow.dotapk.jit.redarrow;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class setActivityHos extends AppCompatActivity {

    Spinner spDefBlood, spDefRadius;
    TextView defBlood,defRadius;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_activity_hos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spDefBlood=(Spinner) findViewById(R.id.spDefBlood);
        spDefRadius=(Spinner) findViewById(R.id.spDefDist);
        defBlood=(TextView) findViewById(R.id.tvDefaultBlood);
        defRadius=(TextView) findViewById(R.id.tvDefaultRadius);
        update=(Button) findViewById(R.id.bUpdateSettings);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.defblood_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDefBlood.setAdapter(adapter);


        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.defradius, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDefRadius.setAdapter(adapter1);

        SharedPreferences sharedPreferences=getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        int langno=sharedPreferences.getInt("langno", 0);
        defBlood.setText(MainActivity.hashMap.get("defaultblood").get(langno));
        defRadius.setText(MainActivity.hashMap.get("defaultradius").get(langno));
        update.setText(MainActivity.hashMap.get("update").get(langno));


        String defbldname=sharedPreferences.getString("defbloodname", "aaassa");
        if(defbldname.equals("aaassa"))
        {
            String selectblood = spDefBlood.getSelectedItem().toString();
            String selectradius = spDefRadius.getSelectedItem().toString();
            int selectbloodpos=spDefBlood.getSelectedItemPosition();
            int selectradiuspos=spDefRadius.getSelectedItemPosition();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("defbloodname",selectblood);
            editor.putString("defradiusname",selectradius);
            editor.putInt("defradiuspos", selectbloodpos);
            editor.putInt("defradiusname", selectradiuspos);
        }
        else
        {
            spDefRadius.setSelection(sharedPreferences.getInt("defradiuspos",0));
            spDefBlood.setSelection(sharedPreferences.getInt("defbloodpos",0));

        }


    }
    public void updateSettings(View v)
    {

        String selectblood = spDefBlood.getSelectedItem().toString();
        String selectradius = spDefRadius.getSelectedItem().toString();
        int selectbloodpos=spDefBlood.getSelectedItemPosition();
        int selectradiuspos=spDefRadius.getSelectedItemPosition();

        SharedPreferences sharedPreferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("defbloodname",selectblood);
        editor.putString("defradiusname",selectradius);
        editor.putInt("defradiuspos", selectradiuspos);
        editor.putInt("defbloodpos", selectbloodpos);
        editor.commit();
        Toast.makeText(getApplicationContext(),"Default Values Updated",Toast.LENGTH_SHORT).show();
    }

}
