package axismobile.service.testingonnboard.system;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class MkSr extends Service {


    private TestReceiver testReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("mywork", "BackgroundService started");
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        testReceiver = new TestReceiver();
        registerReceiver(testReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (testReceiver != null) {
            Log.d("mywork", "Unregistering SmsReceiver");
            unregisterReceiver(testReceiver);
            testReceiver = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // This method is not used for started services
        return null;
    }
}
