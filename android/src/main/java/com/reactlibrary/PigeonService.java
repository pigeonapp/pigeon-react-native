package com.reactlibrary;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public abstract class PigeonService extends Service {

    abstract void onMessageReceived(RemoteMessage remoteMessage);

    private Service underlyingService = new WrappedFirebaseMessagingService();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return underlyingService.onBind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return underlyingService.onStartCommand(intent, flags, startId);
    }
}

/**
 * We do this for two reasons:
 * 1. we want to handle the notifications Pusher can send to check if a device is still online silently
 * 2. we want to support `setOnMessageReceivedListenerForVisibleActivity`
 */
class WrappedFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = WrappedFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived");
    }
}

class EmptyMessagingService extends PigeonService {

    public static final String TAG = EmptyMessagingService.class.getSimpleName();

    @Override
    void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived");
    }
}