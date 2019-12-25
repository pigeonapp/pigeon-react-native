package io.pigeonapp;

import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class PigeonModule extends ReactContextBaseJavaModule {

    private final String TAG = PigeonModule.class.getSimpleName();

    private final ReactApplicationContext reactContext;

    public PigeonModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "Pigeon";
    }

    @ReactMethod
    public void setup(ReadableMap config) {
        String publicKey = config.getString("publicKey");

        PigeonClient.getInstance().setPublicKey(publicKey);
    }

    @ReactMethod
    public void setCustomerToken(String customerToken) {
        PigeonClient.getInstance().setCustomerToken(customerToken);

        if (customerToken != null) {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }

                            // Save new Instance ID token
                            String deviceToken = task.getResult().getToken();
                            PigeonClient.getInstance().setDeviceToken(deviceToken);
                        }
                    });
        }
    }
}
