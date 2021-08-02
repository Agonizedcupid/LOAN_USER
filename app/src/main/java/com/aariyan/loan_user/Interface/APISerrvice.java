package com.aariyan.loan_user.Interface;


import com.aariyan.loan_user.Notification.MyResponse;
import com.aariyan.loan_user.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface APISerrvice {


    @Headers(
            {
                    "Content_Type:application/json",
                    "Authorization:key=AAAAaZpMVCM:APA91bGDUz-A-3PzVmpCQk0DemzLevU2TCj1WzOxHU65mciDwdUI5PnXp3W_Ugw1HQbP6teT_SSQzXZe6wkY8X1WW-F4Wjxvi4JtiZzkk9eTVhxxPaN7K_4-oCjPov8TQZz9LvovBv8s"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
