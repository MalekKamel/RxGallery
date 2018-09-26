package com.sha.kamel.rxgallery;

import android.net.Uri;

import java.util.List;

public class Request {
    private Source source;
    private List<RxGallery.MimeType> mimeTypes;
    private boolean multiSelectEnabled;
    private Uri outputUri;

    public enum Source {
        GALLERY,
        IMAGE_CAPTURE,
        VIDEO_CAPTURE
    }

    public Source getSource() {
        return source;
    }

    public Request setSource(Source source) {
        this.source = source;
        return this;
    }

    public List<RxGallery.MimeType> getMimeTypes() {
        return mimeTypes;
    }

    public Request setMimeTypes(List<RxGallery.MimeType> mimeTypes) {
        this.mimeTypes = mimeTypes;
        return this;
    }

    public boolean isMultiSelectEnabled() {
        return multiSelectEnabled;
    }

    public Request setMultiSelectEnabled(boolean multiSelectEnabled) {
        this.multiSelectEnabled = multiSelectEnabled;
        return this;
    }

    public Request setOutputUri(Uri outputUri) {
        this.outputUri = outputUri;
        return this;
    }
}
