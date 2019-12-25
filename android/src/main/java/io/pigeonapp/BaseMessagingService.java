package io.pigeonapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public abstract class BaseMessagingService extends Service {
    public abstract void onNewToken(String token);

    public abstract void onMessageReceived(RemoteMessage remoteMessage);

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

    /**
     * Use underlyingService in order to handle silent push notifications
     */
    private class WrappedFirebaseMessagingService extends FirebaseMessagingService {
        @Override
        public void onNewToken(@NonNull String token) {
            super.onNewToken(token);

            PigeonClient.getInstance().setDeviceToken(token);
        }

        @Override
        public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
            super.onMessageReceived(remoteMessage);
        }
    }
}
