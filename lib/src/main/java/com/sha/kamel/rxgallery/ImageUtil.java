package com.sha.kamel.rxgallery;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.tbruyelle.rxpermissions2.RxPermissions;



/**
 * Created by it on 11/13/2017.
 */

class ImageUtil {
    public static final int REQUEST_CODE = 55;

    public static void request(Fragment fragment){
        new RxPermissions(fragment.getActivity())
                .request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        doRequest(fragment);
                    }
                });
    }

    public static void doRequest(Fragment fragment) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fragment.startActivityForResult(intent, REQUEST_CODE);
    }

    public static Result result(Intent data) {
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        return new Result().setBitmap(bitmap);
    }
}
