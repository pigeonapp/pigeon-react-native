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

    private final PigeonClient pigeonClient;

    public PigeonModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.pigeonClient = PigeonClient.getInstance();
    }

    @Override
    public String getName() {
        return "Pigeon";
    }

    @ReactMethod
    public void setLogLevel(int logLevel) {
        PigeonLog.setLogLevel(logLevel);
    }

    @ReactMethod
    public void setup(ReadableMap config) {
        String publicKey = config.getString("publicKey");
        pigeonClient.setPublicKey(publicKey);
    }

    @ReactMethod
    public void track(String event, String customerUid){
        pigeonClient.track(event, customerUid);
    }

    @ReactMethod
    public void setCustomerToken(String customerToken) {
        pigeonClient.setCustomerToken(customerToken);

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
