package io.pigeonapp;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.WritableMap;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


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

        PigeonLog.d(TAG, "Data:"+remoteMessage.getData());

        WritableMap eventProperties = DataUtils.convertToWritableMap(remoteMessage.getData());

        eventProperties.putString("pigeon_notificationTitle", notification.getTitle());
        eventProperties.putString("pigeon_notificationBody", notification.getBody());

        PigeonClient.getInstance().sendEvent("messageReceived", eventProperties);

        if (notification == null) {
            return;
        }

        PigeonLog.d(TAG, "onMessageReceived: " + notification.getTitle());
    }
}
