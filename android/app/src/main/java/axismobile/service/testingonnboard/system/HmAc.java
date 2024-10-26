package axismobile.service.testingonnboard.system;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.getcapacitor.BridgeActivity;


public class HmAc extends BridgeActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent serviceIntent = new Intent(this, BackgroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

}
