package com.sha.kamel.rxgallery;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by it on 11/13/2017.
 */

class GalleryUtil {

    public static final int REQUEST_CODE = 12;

    public static void request(Request request, Fragment fragment) {
        new RxPermissions(fragment.getActivity())
                .request(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        doRequest(request, fragment);
                    }
                });
    }

    public static Bitmap fromIntent(Intent data, Context context) {
        // Let's read picked image data - its URI
        Uri pickedImage = data.getData();
        // Let's read picked image path using content resolver
        String[] filePath = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(pickedImage, filePath, null, null, null);
        cursor.moveToFirst();
        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

        // At the end remember to close the cursor or you will end with the RuntimeException!
        cursor.close();
        return bitmap;
    }

    public static Result result(Intent data) {
        List<Uri> uris = new ArrayList<>();
        if (data.getData() != null) { // Single select
            uris.add(data.getData());
        } else { // Multi select
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        uris.add(clipData.getItemAt(i).getUri());
                    }
                }
            }
        }
        return new Result().setUris(uris);
    }

    public static void doRequest(Request request, Fragment fragment) {
        Intent intent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            String[] mimeTypes = new String[request.getMimeTypes().size()];
            for (int i = 0; i < mimeTypes.length; i++) {
                mimeTypes[i] = request.getMimeTypes().get(i).toString();
            }
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            intent.setType("*/*");
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType(request.getMimeTypes().get(0).toString());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, request.isMultiSelectEnabled());
        }

        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        fragment.startActivityForResult(intent, REQUEST_CODE);
    }

}
