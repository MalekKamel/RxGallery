package com.sha.kamel.rxgallery;

import android.Manifest;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Arrays;

class VideoUtil {
    public static final int REQUEST_CODE = 550;

    public static void request(Fragment fragment) {
        new RxPermissions(fragment.getActivity())
                .request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted)
                        doRequest(fragment);
                });
    }

    public static void doRequest(Fragment fragment) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        fragment.startActivityForResult(intent, REQUEST_CODE);
    }

    public static Result result(Intent intent){
       return new Result().setUris(Arrays.asList(intent.getData()));
    }
}
