package com.martin.httputil.image;

import android.widget.ImageView;

/**
 * Desc:
 * Author:Martin
 * Date:2016/8/1
 */
public class ImageImpl {

    private static volatile ImageImpl sInstance;
    private ImageStrategy imageStrategy;

    private ImageImpl() {
    }

    public void setStrategy(ImageStrategy imageStrategy) {
        this.imageStrategy = imageStrategy;
    }

    public static ImageImpl instance() {
        if (sInstance == null) {
            synchronized (ImageImpl.class) {
                if (sInstance == null) {
                    sInstance = new ImageImpl();
                }
            }
        }
        return sInstance;
    }

    public void display(String url, ImageView imageView) {

    }
}
