package uber.com.myapplication.rest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PhotoLoader {
    ExecutorService executors;
    LruCache<String,Bitmap> lruCache;

    public PhotoLoader() {
        executors = Executors.newFixedThreadPool(6);
        initializeCache();
    }

    private void initializeCache(){
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        lruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void displayImage(ImageView imageView, String url) {
        executors.submit(new PhotoRunnable(imageView,url));

    }

    private class PhotoRunnable implements Runnable {
        private String INNER_TAG = PhotoRunnable.class.getName();
        private SoftReference<ImageView> imageViwReference;
        private String url;

        public PhotoRunnable(ImageView imageView, String url) {
            this.imageViwReference = new SoftReference<>(imageView);
            this.url = url;
        }

        @Override
        public void run() {
            final Bitmap bitmap = lruCache.get(this.url);
            if(bitmap!= null){
                final ImageView imageView = imageViwReference.get();
                if (imageView != null) {
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                }
                return;
            }
            InputStream is = null;
            try {
                URL url = new URL(this.url);
                URLConnection connection = url.openConnection();
                is = connection.getInputStream();
                final Bitmap decodedBitmap = BitmapFactory.decodeStream(is);
                lruCache.put(this.url,decodedBitmap);
                final ImageView imageView = imageViwReference.get();
                if (imageView != null) {
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(decodedBitmap);
                        }
                    });
                }
            } catch (IOException ex) {
                Log.d(INNER_TAG, "Exception in run method " + ex.getMessage());
            } finally {
                try {
                    is.close();
                } catch (IOException ex) {
                    Log.d(INNER_TAG, "Exception in closing stream " + ex.getMessage());
                }
            }

        }
    }

}
