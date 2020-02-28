package io.pigeonapp;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.lang.reflect.MalformedParametersException;
import java.util.Iterator;
import java.util.Map;


public class PigeonMessagingService extends FirebaseMessagingService {
    private final String TAG = PigeonMessagingService.class.getSimpleName();

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        PigeonLog.d(TAG, "onNewToken: " + token);

        PigeonClient.getInstance().setDeviceToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        RemoteMessage.Notification notification = remoteMessage.getNotification();

        Callback onMessageReceivedCallback = PigeonClient.getInstance().getOnMessageReceivedCallback();

        if(onMessageReceivedCallback != null && remoteMessage.getData().size() > 0){
            onMessageReceivedCallback.invoke(DataUtilsKt.convertToWritableMap(remoteMessage.getData()));
        }

        if (notification == null) {
            return;
        }

        PigeonLog.d(TAG, "onMessageReceived: " + notification.getTitle());
    }
}
