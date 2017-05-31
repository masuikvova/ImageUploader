package com.binopt.imageuploader;

import com.readystatesoftware.chuck.ChuckInterceptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ImageUploader {
    public interface OnImageUploadListener {
        void uploadingSuccess(String response);

        void uploadingError(String response);
    }

    private byte[] imageBytes;
    private String fileName;
    private String boundary;
    private OnImageUploadListener listener;
    public static final String URL = "https://posttestserver.com/post.php";

    public ImageUploader(String username, byte[] imageBytes) {
        this.imageBytes = imageBytes;
        fileName = username + ".jpg";
        boundary = "---------" + System.currentTimeMillis();
    }

    public void uploadImage() {
        sendImageRequest();
    }

    public void addOnImageUploadListener(OnImageUploadListener listener) {
        this.listener = listener;
    }

    private void sendImageRequest() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new ChuckInterceptor(UploaderApplication.getInstance())); //add interceptor
        builder.connectTimeout(1, TimeUnit.MINUTES);
        OkHttpClient okHttpClient = builder.build();

        okHttpClient.newCall(createRequest()).enqueue(new UIThreadCallback() {
            @Override
            public void onFail(Exception error) {
                listener.uploadingError("Error uploading image");
            }

            @Override
            public void onSuccess(String responseBody) {
                listener.uploadingSuccess(responseBody);
            }
        });
    }

    private Request createRequest() {
        String requestPostBodyString = fileName + "54235234";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"data\""), RequestBody.create(null, requestPostBodyString))
                .addPart(Headers.of("Content-Disposition", String.format("form-data; name=\"file\"; filename=\"%s\"", fileName)),
                        RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), imageBytes))
                .build();

        return new Request.Builder()
                .addHeader("Content-Type", "multipart/form-data; boundary=" + boundary)
                .post(requestBody)
                .addHeader("Content-Length", String.valueOf(calculateBodyDataLength(requestPostBodyString)))
                .tag("post")
                .url(URL)
                .build();
    }

    private int calculateBodyDataLength(String requestPostBodyString) {
        int dataLength = 0;
        try {
            byte[] bodyStringBytes = requestPostBodyString.getBytes("UTF-8");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(bodyStringBytes);
            outputStream.write(bodyStringBytes);
            dataLength = outputStream.toByteArray().length;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataLength;
    }
}
