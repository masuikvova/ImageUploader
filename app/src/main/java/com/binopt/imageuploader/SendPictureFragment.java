package com.binopt.imageuploader;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.ByteArrayOutputStream;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class SendPictureFragment extends Fragment {

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnCam)
    void camButton(){
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_at);
        startUploadService(bitmapToByteArray(largeIcon));
    }

    @OnClick(R.id.btnLib)
    void libraryButton(){

    }

    private void startUploadService( byte[] imageInBytes) {
        Intent serviceIntent = new Intent(getActivity(), ImageUploadService.class);
        serviceIntent.putExtra(ImageUploadService.PICTURE, imageInBytes);
        getActivity().startService(serviceIntent);
    }

    private static byte[] bitmapToByteArray(Bitmap bitmap) {
        byte[] result;
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            result = stream.toByteArray();
        } else {
            result = new byte[0];
        }
        return result;
    }
}
