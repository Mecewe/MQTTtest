package com.mecewe.mqtttest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.mecewe.mqtttest.MainActivity.JSON;


public class HttpUtil {

    public static void sendOkHttpPost(String address,String method,String params,okhttp3.Callback callback){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put(method,params);
//            jsonObject.put("params",params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final RequestBody requestBody = RequestBody.create(JSON, String.valueOf(jsonObject));
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .writeTimeout(8, TimeUnit.SECONDS)
                .readTimeout(8, TimeUnit.SECONDS)
                .build();
        Request request=new Request.Builder()
                .url(address)
                .header("Content-Type", "application/json")
                .addHeader("Accept","application/json")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
