package io.pigeonapp;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Arrays;

public class PigeonMessagingService extends FirebaseMessagingService {
    private final String TAG = PigeonMessagingService.class.getSimpleName();
    private final String PIGEON_FILTER_KEY = "pigeon_pn_type";

    private PigeonClient pigeonClient;

    public PigeonMessagingService() {
        super();
        pigeonClient = PigeonClient.getInstance();
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        PigeonLog.d(TAG, "onNewToken: " + token);

        pigeonClient.setDeviceToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        RemoteMessage.Notification notification = remoteMessage.getNotification();

        PigeonLog.d(TAG, "Data:" + remoteMessage.getData());

        WritableMap eventProperties = Arguments.createMap();

        try {
            eventProperties.putMap("data", DataUtils.convertToWritableMap(remoteMessage.getData(), Arrays.asList(PIGEON_FILTER_KEY)));
        } catch (Exception e) {
            PigeonLog.d(TAG, "Encountered an error while parsing notification data");
            e.printStackTrace();
        }

        if (notification != null) {
            WritableMap notificationProperties = Arguments.createMap();
            notificationProperties.putString("title", notification.getTitle());
            notificationProperties.putString("body", notification.getBody());
            eventProperties.putMap("notification", notificationProperties);
            PigeonLog.d(TAG, "onMessageReceived: " + notification.getTitle());
        }

        if (remoteMessage.getData().containsKey(PIGEON_FILTER_KEY)) {
            pigeonClient.sendEvent("messageReceived", eventProperties);
        }
    }
}
