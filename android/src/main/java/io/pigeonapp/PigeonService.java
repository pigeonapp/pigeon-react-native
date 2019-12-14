package io.pigeonapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import io.pigeonapp.config.PigeonApiProvider;
import io.pigeonapp.model.Employee;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class PigeonService extends Service {

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

        PigeonApiProvider.get().getEmployees()
            .enqueue(new Callback<List<Employee>>() {
                @Override
                public void onResponse(Call<List<Employee>> employees, Response<List<Employee>> response) {
                    Log.d(TAG, employees.toString());
                }

                @Override
                public void onFailure(Call<List<Employee>> employees, Throwable t) {

                }
            });
    }
}

class EmptyMessagingService extends PigeonService {

    public static final String TAG = EmptyMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived");
    }
}
