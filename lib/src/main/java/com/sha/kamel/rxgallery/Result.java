package com.sha.kamel.rxgallery;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.List;

class Result {
    private List<Uri> uris;
    private Bitmap bitmap;

    public List<Uri> getUris() {
        return uris;
    }

    public Result setUris(List<Uri> uris) {
        this.uris = uris;
        return this;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Result setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        return this;
    }
}
