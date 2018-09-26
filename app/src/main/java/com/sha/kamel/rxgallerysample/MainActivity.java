package com.sha.kamel.rxgallerysample;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.annimon.stream.Stream;
import com.sha.kamel.rxgallery.RxGallery;

import java.util.Arrays;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private Disposable disposable;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);

        Button takeVideoButton = findViewById(R.id.take_video_btn);
        takeVideoButton.setEnabled(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public void pickImage(View view) {
        disposable = new RxGallery()
                .image(this)
                .subscribe(uri -> print(uri));
    }

    public void pickMultipleImages(View view) {
        disposable = new RxGallery()
                .multipleImages(this)
                .subscribe(uri -> print(uri));
    }

      public void pickAudio(View view) {
        disposable = new RxGallery()
                .audio(this)
                .subscribe(uri -> print(uri));
    }

    public void pickMultipleAudio(View view) {
        disposable = new RxGallery()
                .multipleAudio(this)
                .subscribe(uri -> print(uri));
    }

    public void pickVideo(View view) {
        disposable = new RxGallery()
                .video(this)
                .subscribe(uri -> print(uri));
    }

    public void pickMultipleVideos(View view) {
        disposable = new RxGallery()
                .multipleVideos(this)
                .subscribe(uri -> print(uri));
    }

    public void takeVideo(View view) {
        disposable = new RxGallery()
                .captureVideo(this)
                .subscribe(uri -> print(uri));
    }

    public void takePhoto(View view) {
        disposable = new RxGallery()
                .captureImage(this)
                .subscribe(bitmap -> imageView.setImageBitmap(bitmap));
    }

    private void print(Uri uri) {
        print(Arrays.asList(uri));
    }

    private void print(List<Uri> list) {
        Stream.of(list).forEach(System.out::println);
    }

}
