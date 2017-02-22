package com.example.oliverasker.skywarnmarkii.Cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by oliverasker on 2/20/17.
 * https://developer.android.com/topic/performance/graphics/cache-bitmap.html
 */

public class LRUCacheSingleton {

    private final int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
    private final int cacheSize = maxMemory/8;
    private LruCache<String,Bitmap> mMemoryCache;
    private static LRUCacheSingleton lruCache = new LRUCacheSingleton();

    public LRUCacheSingleton(){

    }

    public static LRUCacheSingleton getInstance(){
        if(lruCache == null){
            return new LRUCacheSingleton();
        }
        else
            return lruCache;
    }


    public void initCache(){
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap){
                return bitmap.getByteCount()/1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap){
        if(getBitmapFromMemCache(key) == null){
            mMemoryCache.put(key,bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key){
        return mMemoryCache.get(key);
    }
}
