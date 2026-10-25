package com.mymonkmindset.openingstock.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NetworkClient {
    private static final OkHttpClient client = new OkHttpClient();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface HttpResponseCallback {
        void onSuccess(String responseBody);
        void onFailure(String errorMessage);
    }

    // 1. GET Request
    public static void makeGetRequest(String url, HttpResponseCallback callback) {
        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                mainHandler.post(() -> callback.onFailure(e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    try (ResponseBody responseBody = response.body()) {
                        String responseData = responseBody.string();
                        mainHandler.post(() -> callback.onSuccess(responseData));
                    } catch (IOException e) {
                        mainHandler.post(() -> callback.onFailure("Failed to read response body: " + e.getMessage()));
                    }
                } else {
                    mainHandler.post(() -> callback.onFailure("Server error code: " + response.code()));
                }
            }
        });
    }

    // 2. POST Request (Sending JSON Body)
    public static void makePostRequest(String url, String jsonPayload, HttpResponseCallback callback) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonPayload, JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                mainHandler.post(() -> callback.onFailure(e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    try (ResponseBody responseBody = response.body()) {
                        String responseData = responseBody.string();
                        mainHandler.post(() -> callback.onSuccess(responseData));
                    } catch (IOException e) {
                        mainHandler.post(() -> callback.onFailure("Failed to read response body: " + e.getMessage()));
                    }
                } else {
                    mainHandler.post(() -> callback.onFailure("Server error code: " + response.code()));
                }
            }
        });
    }
}
