package redarrow.dotapk.jit.redarrow;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by jit on 27-01-2017.
 */
public class FcmIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences preferences = getSharedPreferences("Fcm_token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fcmtoken",recent_token);
        editor.commit();

    }
}
