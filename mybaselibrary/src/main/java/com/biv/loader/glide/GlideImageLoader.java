/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Piasy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.biv.loader.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.biv.loader.ImageLoader;
import com.biv.view.BigImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fivefivelike.mybaselibrary.R;

import java.io.File;

import okhttp3.OkHttpClient;

/**
 * Created by Piasy{github.com/Piasy} on 09/11/2016.
 */

public final class GlideImageLoader implements ImageLoader {
    private final RequestManager mRequestManager;

    private GlideImageLoader(Context context, OkHttpClient okHttpClient) {
        GlideProgressSupport.init(Glide.get(context), okHttpClient);
        mRequestManager = Glide.with(context);
    }

    public static GlideImageLoader with(Context context) {
        return with(context, null);
    }

    public static GlideImageLoader with(Context context, OkHttpClient okHttpClient) {
        return new GlideImageLoader(context, okHttpClient);
    }

    @Override
    public void loadImage(final String uri, final Callback callback) {
        mRequestManager
                .downloadOnly()
                .load(uri)
                .into(new ImageDownloadTarget(uri.toString()) {
                    @Override
                    public void onResourceReady(File resource,
                                                Transition<? super File> transition) {
                        // we don't need delete this image file, so it behaves live cache hit
                        callback.onCacheHit(resource);
                        callback.onSuccess(resource);
                    }

                    @Override
                    public void onLoadFailed(final Drawable errorDrawable) {
                        callback.onFail(new GlideLoaderException(errorDrawable));
                    }

                    @Override
                    public void onDownloadStart() {
                        callback.onStart();
                    }

                    @Override
                    public void onProgress(int progress) {
                        callback.onProgress(progress);
                    }

                    @Override
                    public void onDownloadFinish() {
                        callback.onFinish();
                    }
                });
    }

    @Override
    public View showThumbnail(BigImageView parent, String thumbnail, int scaleType) {
        ImageView thumbnailView = (ImageView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ui_glide_thumbnail, parent, false);
        switch (scaleType) {
            case BigImageView.INIT_SCALE_TYPE_CENTER_CROP:
                thumbnailView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;
            case BigImageView.INIT_SCALE_TYPE_CENTER_INSIDE:
                thumbnailView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            default:
                break;
        }
        mRequestManager
                .load(thumbnail)
                .into(thumbnailView);
        return thumbnailView;
    }


    @Override
    public void prefetch(Uri uri) {
        mRequestManager
                .downloadOnly()
                .load(uri)
                .into(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File resource,
                                                Transition<? super File> transition) {
                        // not interested in result
                    }
                });
    }
}
