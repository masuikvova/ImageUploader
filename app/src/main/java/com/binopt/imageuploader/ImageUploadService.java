package com.binopt.imageuploader;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class ImageUploadService extends IntentService implements ImageUploader.OnImageUploadListener {
    public static final String PICTURE = "picture";

    public ImageUploadService() {
        super("ImageUploadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            byte[] releasedImageByteArray = intent.getByteArrayExtra(PICTURE);
            if (releasedImageByteArray.length > 0) {
                initImageUploader("user_name_or_id", releasedImageByteArray);
            }
        }
    }

    private void initImageUploader(String user, byte[] imageInBytes) {
        ImageUploader imageUploader = new ImageUploader(user, imageInBytes);
        imageUploader.addOnImageUploadListener(this);
        imageUploader.uploadImage();
    }

    @Override
    public void uploadingSuccess(String response) {
        Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
    }

    @Override
    public void uploadingError(String response) {
        Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
    }
}
