package com.reactlibrary.network;

import com.reactlibrary.model.Employee;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface PigeonApi {

    @GET("employees")
    Call<List<Employee>> getEmployees();
}