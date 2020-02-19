package io.pigeonapp;

import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

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

        Log.d(TAG, "setCustomerToken: " + customerToken);

        saveContact();
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;

        Log.d(TAG, "setDeviceToken: " + deviceToken);

        saveContact();
    }

    private void saveContact() {
        if (deviceToken == null || customerToken == null || publicKey == null) {
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
                Log.d(TAG, "Could not save contact: " + deviceName + " " + deviceKind + " " + deviceToken);

                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Encountered an error during saveContact()");
                    return;
                }

                GenericResponse genericResponse = gson.fromJson(response.body().string(), GenericResponse.class);
                if (!genericResponse.success) {
                    Log.d(TAG, "Encountered an error during saveContact()");
                    return;
                }

                Log.d(TAG, "Saved contact: " + deviceName + " " + deviceKind + " " + deviceToken);
            }
        });
    }

    private class SaveContactRequest {
        @SerializedName("name")
        private String name;

        @SerializedName("kind")
        private String kind;

        @SerializedName("value")
        private String value;

        public SaveContactRequest(String name, String kind, String value) {
            this.name = name;
            this.kind = kind;
            this.value = value;
        }
    }

    private class GenericResponse {
        @SerializedName("success")
        private Boolean success;
    }
}
