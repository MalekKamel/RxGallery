package com.sha.kamel.rxgallery;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.annimon.stream.function.Consumer;

public class RxGalleryFrag extends Fragment {

    private Consumer<Result> callback;

    public static RxGalleryFrag newInstance() {
        RxGalleryFrag frag = new RxGalleryFrag();
        return frag;
    }

    public void start(Request request) {
        switch (request.getSource()) {
            case GALLERY:
                GalleryUtil.request(request, this);
                break;
            case VIDEO_CAPTURE:
                VideoUtil.request(this);
                break;
            case IMAGE_CAPTURE:
                ImageUtil.request(this);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GalleryUtil.REQUEST_CODE:
                    callback.accept(GalleryUtil.result(data));
                    break;

                case VideoUtil.REQUEST_CODE:
                    callback.accept(VideoUtil.result(data));
                    break;

                case ImageUtil.REQUEST_CODE:
                    callback.accept(ImageUtil.result(data));
                    break;
            }
        }
    }

    public void listenToResult(Consumer<Result> callback) {
        this.callback = callback;
    }
}
