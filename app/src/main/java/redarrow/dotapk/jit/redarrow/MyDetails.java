package redarrow.dotapk.jit.redarrow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyDetails extends AppCompatActivity {
    TextView dob1,name1,blood1,address1,health1,contact1;
    Donor donor;

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        donor= (Donor)getIntent().getSerializableExtra("Donor");
        String blood = donor.getBlood_type();
        String dob = donor.getDob();
        String address =donor.getAddress();
        String contact =donor.getContact_no();
        String health_issues = donor.getHealth_issues();
        String name =donor.getName();



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
        blood1=(TextView)findViewById(R.id.donor_blood);
        name1=(TextView)findViewById(R.id.userName);
        dob1=(TextView)findViewById(R.id.donor_dob);
        address1=(TextView)findViewById(R.id.donor_address);
        health1=(TextView)findViewById(R.id.donor_health);
        contact1=(TextView)findViewById(R.id.donor_contact);
        blood1.setText(blood);
        name1.setText(name);
        address1.setText(address);
        dob1.setText(dob);
        contact1.setText(contact);
        health1.setText(health_issues);
        changelang();

        Button editbtn=(Button)findViewById(R.id.bEdit);
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),EditUser.class);
                intent.putExtra("Donor",donor);
                startActivity(intent);
               // startActivity(new Intent(getApplicationContext(),EditUser.class));
            }
        });

    }

    public void changelang()
    {
        SharedPreferences preferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        int langno=preferences.getInt("langno", 0);
        ((TextView)findViewById(R.id.langBloodType)).setText(MainActivity.hashMap.get("blood type").get(langno));
        ((TextView)findViewById(R.id.langAddress)).setText(MainActivity.hashMap.get("address").get(langno));
        ((TextView)findViewById(R.id.langContact)).setText(MainActivity.hashMap.get("contact").get(langno));
        ((TextView)findViewById(R.id.langDOB)).setText(MainActivity.hashMap.get("date of birth").get(langno));
        ((TextView)findViewById(R.id.langHealth)).setText(MainActivity.hashMap.get("healthissues").get(langno));
        ((Button)findViewById(R.id.bEdit)).setText(MainActivity.hashMap.get("edit").get(langno));
    }
}
