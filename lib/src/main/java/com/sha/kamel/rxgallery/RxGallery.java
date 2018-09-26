package com.sha.kamel.rxgallery;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.processors.PublishProcessor;

public class RxGallery {

    private PublishProcessor<Result> pp = PublishProcessor.create();
    private RxGalleryFrag frag;

    public enum MimeType {
        IMAGE("image/*"),
        VIDEO("video/*"),
        AUDIO("audio/*");

        private final String value;

        MimeType(String mimeTypeString) {
            this.value = mimeTypeString;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * Select an image form Gallery.
     *
     * @param activity An Activity to open gallery from.
     * @return A Single which calls onSuccess with the Uris of selected gallery items.
     */
    public Single<Uri> image(@NonNull FragmentActivity activity) {
        return requestImage(
                activity,
                false,
                MimeType.IMAGE)
                .map(uris -> uris.get(0));
    }

    /**
     * Select multiple images form Gallery.
     *
     * @param activity Activity.
     * @return A Single .
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public Single<List<Uri>> multipleImages(@NonNull FragmentActivity activity) {
        return requestImage(activity, true, MimeType.IMAGE);
    }


    /**
     * Select an image form Gallery.
     *
     * @param activity An Activity to open gallery from.
     * @return A Single which calls onSuccess with the Uris of selected gallery items.
     */
    public Single<Uri> audio(@NonNull FragmentActivity activity) {
        return requestImage(
                activity,
                false,
                MimeType.AUDIO)
                .map(uris -> uris.get(0));
    }

    /**
     * Select multiple images form Gallery.
     *
     * @param activity Activity.
     * @return A Single .
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public Single<List<Uri>> multipleAudio(@NonNull FragmentActivity activity) {
        return requestImage(activity, true, MimeType.AUDIO);
    }

    /**
     * Select an image form Gallery.
     *
     * @param activity An Activity to open gallery from.
     * @return A Single which calls onSuccess with the Uris of selected gallery items.
     */
    public Single<Uri> video(@NonNull FragmentActivity activity) {
        return requestImage(
                activity,
                false,
                MimeType.VIDEO)
                .map(uris -> uris.get(0)
                );
    }

    /**
     * Select multiple images form Gallery.
     *
     * @param activity Activity.
     * @return A Single .
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public Single<List<Uri>> multipleVideos(@NonNull FragmentActivity activity) {
        return requestImage(activity, true, MimeType.VIDEO);
    }

    /**
     * Select an image form Gallery.
     *
     * @param activity An Activity to open gallery from.
     * @return A Single which calls onSuccess with the Uris of selected gallery items.
     */
    public Single<Uri> requestByType(@NonNull FragmentActivity activity, MimeType... mimeType) {
        return requestImage(
                activity,
                false,
                mimeType)
                .map(uris -> uris.get(0)
                );
    }

    /**
     * Select multiple images form Gallery.
     *
     * @param activity Activity.
     * @return A Single .
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public Single<List<Uri>> requestMultipleByType(@NonNull FragmentActivity activity, MimeType... mimeType) {
        return requestImage(
                activity,
                true,
                mimeType
        );
    }

    /**
     * capture an image from Camera.
     *
     * @param activity  An Activity to open photo capture from.
     * @return A Single which calls onSuccess with the Uri of captured photo.
     */
    public Single<Bitmap> captureImage(@NonNull FragmentActivity activity) {
        Request request = new Request()
                .setSource(Request.Source.IMAGE_CAPTURE);
        return request(activity, request)
                .map(Result::getBitmap);
    }

    /**
     * Returns a Single for a video capture.
     *
     * @param activity An Activity to open video capture from.
     * @return A Single which calls onSuccess with the Uri of captured video.
     */
    public Single<Uri> captureVideo(@NonNull FragmentActivity activity) {
        Request request = new Request()
                .setSource(Request.Source.VIDEO_CAPTURE);
        return request(activity, request)
                .map(Result::getUris)
                .map(uris -> uris.get(0));
    }

    private Single<Result> request(@NonNull final FragmentActivity activity, @NonNull final Request request) {
        addFragment(activity.getSupportFragmentManager(), request);
        frag.listenToResult(result -> {
            pp.onNext(result);
            pp.onComplete();
        });
        return Single.fromPublisher(pp);
    }

    private Single<List<Uri>> requestImage(
            @NonNull FragmentActivity activity,
            boolean multiSelectEnabled,
            @Nullable MimeType... mimeTypes) {
        Request request = new Request()
                .setSource(Request.Source.GALLERY)
                .setMultiSelectEnabled(multiSelectEnabled)
                .setMimeTypes(Arrays.asList(mimeTypes));
        return request(activity, request).map(Result::getUris);
    }

    private synchronized void addFragment(@NonNull final FragmentManager fragmentManager, Request request) {
        frag = findFragment(fragmentManager);
        if (frag == null) {
            frag = RxGalleryFrag.newInstance();
            fragmentManager
                    .beginTransaction()
                    .add(frag, RxGalleryFrag.class.getSimpleName())
                    .commitNowAllowingStateLoss();
        }
        startActivityForResult(frag, request);
    }

    private void startActivityForResult(RxGalleryFrag frag, @NonNull final Request request) {
        try {
            frag.start(request);
        } catch (Exception e) {
            // Ignore the error
            e.printStackTrace();
        }
    }

    private RxGalleryFrag findFragment(@NonNull final FragmentManager fragmentManager) {
        return (RxGalleryFrag) fragmentManager.findFragmentByTag(RxGalleryFrag.class.getSimpleName());
    }

}
