package io.pigeonapp;

import android.os.Build;

import com.facebook.react.bridge.ReadableMap;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PigeonClient {
    private static String TAG = "PigeonClient";

    private static PigeonClient instance = null;

    private String baseURI = "https://api.pigeonapp.io/v1";
    private String publicKey;
    private String customerToken;
    private String deviceToken;

    private OkHttpClient httpClient = new OkHttpClient();

    private Gson gson = new Gson();

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static PigeonClient getInstance() {
        if (instance == null) {
            instance = new PigeonClient();
        }

        return instance;
    }

    public void setBaseURI(String baseURI) {
        this.baseURI = baseURI;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setCustomerToken(String customerToken) {
        this.customerToken = customerToken;

        PigeonLog.d(TAG, "setCustomerToken: " + customerToken);

        saveContact();
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;

        PigeonLog.d(TAG, "setDeviceToken: " + deviceToken);

        saveContact();
    }

    public void track(final String event, final ReadableMap data) {
        if (customerToken == null) {
            PigeonLog.d(TAG, "Customer token not set");
            return;
        }

        TrackRequest trackRequest;

        try {
            trackRequest = new TrackRequest(event, DataUtils.convertMapToJson(data));
        } catch (JSONException e) {
            PigeonLog.d(TAG, "Encountered an error while parsing data");
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(gson.toJson(trackRequest), JSON);

        Request request = new Request.Builder()
                .url(baseURI + "/event_logs")
                .addHeader("X-Customer-Token", customerToken)
                .addHeader("X-Public-Key", publicKey)
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                PigeonLog.d(TAG, "Could not track: " + event + " " + data);
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    PigeonLog.d(TAG, "Encountered an error during track():" + response.body().string());
                    return;
                }

                GenericResponse genericResponse = gson.fromJson(response.body().string(), GenericResponse.class);
                if (!genericResponse.getSuccess()) {
                    PigeonLog.d(TAG, "Encountered an error during track():" + genericResponse.toString());
                    return;
                }

                PigeonLog.d(TAG, "Sent event: " + event + " " + data);
            }
        });
    }

    private void saveContact() {
        if (deviceToken == null || customerToken == null) {
            return;
        }

        final String deviceName = Build.BRAND + " " + Build.MODEL;
        final String deviceKind = "android";

        SaveContactRequest saveContactRequest = new SaveContactRequest(deviceName, deviceKind, deviceToken);
        RequestBody body = RequestBody.create(gson.toJson(saveContactRequest), JSON);
        Request request = new Request.Builder()
                .url(baseURI + "/contacts")
                .addHeader("X-Public-Key", publicKey)
                .addHeader("X-Customer-Token", customerToken)
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                PigeonLog.d(TAG, "Could not save contact: " + deviceName + " " + deviceKind + " " + deviceToken);

                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    PigeonLog.d(TAG, "Encountered an error during saveContact()");
                    return;
                }

                GenericResponse genericResponse = gson.fromJson(response.body().string(), GenericResponse.class);
                if (!genericResponse.getSuccess()) {
                    PigeonLog.d(TAG, "Encountered an error during saveContact()");
                    return;
                }

                PigeonLog.d(TAG, "Saved contact: " + deviceName + " " + deviceKind + " " + deviceToken);
            }
        });
    }
}
