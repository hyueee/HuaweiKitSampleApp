package com.example.huaweikitsampleapp;

import com.example.huaweikitsampleapp.FCMResponse;
import com.example.huaweikitsampleapp.FCMSendData;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ICFMService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAH2wd_gc:APA91bHZHnpUWEM2tprMH7kjprOj-3FXJbtWKF8GebMwb9WLxk103bgRfIM5tcRaVmmhHgcnhqlpF-ztbYWlCrI22UD6Eh2cXt6c0sSEVT3y2F1_djX9Jg-XChmzzGFTUKEt9ylHh0JO"
    })

    @POST("fcm/send")
    Observable<FCMResponse> sendNotification(@Body FCMSendData body);
}
