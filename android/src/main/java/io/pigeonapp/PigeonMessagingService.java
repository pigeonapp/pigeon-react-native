package io.pigeonapp;

import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

public class PigeonMessagingService extends BaseMessagingService {
    private final String TAG = PigeonMessagingService.class.getSimpleName();

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "onNewToken: " + token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived: " + remoteMessage.getNotification().getTitle());
    }
}
