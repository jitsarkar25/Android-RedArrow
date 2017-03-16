package redarrow.dotapk.jit.redarrow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ChooseActivity extends AppCompatActivity {
    String name,email,token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        name=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");

        token=getIntent().getStringExtra("token");

        Log.d("Choose",name+email);
    }

    public void donorclick(View v)
    {
        Intent intent=new Intent(this,DonorInfoActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("email",email);

        intent.putExtra("token",token);
        startActivity(intent);
        finish();
    }
    public void hospitalclick(View v)
    {
        Intent intent=new Intent(this,HospitalInfoActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("email",email);
        intent.putExtra("token",token);
        startActivity(intent);
        finish();
    }
}
