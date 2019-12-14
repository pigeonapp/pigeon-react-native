package io.pigeonapp.config;

import io.pigeonapp.network.PigeonApi;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PigeonApiProvider {

    private static Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://dummy.restapiexample.com/api/v1/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    private static final PigeonApi api = retrofit.create(PigeonApi.class);

    // Should be wired properly & also it should be a Singleton
    public static PigeonApi get() {
        return api;
    }
}
