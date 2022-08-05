package com.example.myapplication.Fragments;
import com.example.myapplication.Notifications.MyResponse;
import com.example.myapplication.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAe5SlFw4:APA91bHLaaKoJQKdfR_R_Nz05TFkU2FKRH1zncadY04stnlhZJ24njveed6-mJMYkupZW0J-u6724eFgTx2hVHDNL98mydlz0i74HkXiaMwTc5xGF2ylGfQnpP6ytrs7EUUvIBhmMJf4"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
