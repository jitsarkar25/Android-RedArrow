package redarrow.dotapk.jit.redarrow;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class LnguageChooseActivity extends AppCompatActivity {

    RadioButton rbEng,rbHin,rbBeng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lnguage_choose);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rbEng=(RadioButton)findViewById(R.id.rbEnglish);
        rbHin=(RadioButton)findViewById(R.id.rbHindi);
        rbBeng=(RadioButton)findViewById(R.id.rbBengali);
        SharedPreferences preferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        int langno=preferences.getInt("langno",0);

        ((TextView)findViewById(R.id.tvlanguagechoose )).setText(MainActivity.hashMap.get("choose language").get(langno));
        if(langno==0)
            rbEng.setChecked(true);
        else if(langno==1)
            rbHin.setChecked(true);
        else
            rbBeng.setChecked(true);

    }

    public void languageselect(View v)
    {
        SharedPreferences preferences = getSharedPreferences("RedArrow_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();


        switch(v.getId())
        {
            case R.id.rbEnglish:
                if(rbEng.isChecked())
                {
                    rbHin.setChecked(false);
                    rbBeng.setChecked(false);
                    editor.putString("language", "english");
                    editor.putInt("langno", 0);
                    editor.commit();
                }
                break;
            case R.id.rbHindi:

                if(rbHin.isChecked())
                {
                    rbEng.setChecked(false);
                    rbBeng.setChecked(false);
                    editor.putString("language", "hindi");
                    editor.putInt("langno", 1);
                    editor.commit();
                }
                break;
            case R.id.rbBengali:
                if(rbBeng.isChecked())
                {
                    rbHin.setChecked(false);
                    rbEng.setChecked(false);
                    editor.putString("language", "bengali");
                    editor.putInt("langno", 2);
                    editor.commit();
                }

                break;
        }
        finish();
    }

}
