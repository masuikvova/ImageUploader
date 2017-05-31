package com.binopt.imageuploader;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public abstract class UIThreadCallback implements Callback {
    private final Handler handler;

    public UIThreadCallback() {
        handler = new Handler(Looper.getMainLooper());
    }

    abstract public void onFail(final Exception error);

    abstract public void onSuccess(final String responseBody);

    @Override
    public void onFailure(Call call, final IOException e) {
        executeInUiThread(new Runnable() {
            @Override
            public void run() {
                onFail(e);
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (!response.isSuccessful() || response.body() == null) {
            onFailure(call, new IOException("Failed"));
            return;
        }
        final String responseString = response.body().string();
        executeInUiThread(new Runnable() {
            @Override
            public void run() {
                onSuccess(responseString);
            }
        });
    }

    private void executeInUiThread(Runnable task) {
        handler.post(task);
    }
}
