package com.example.oliverasker.skywarnmarkii;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by oliverasker on 2/20/17.
 * http://www.vogella.com/tutorials/AndroidApplicationOptimization/article.html
 */

public class BitmapUtility {

    public static Bitmap decodeBitmapWidthGiveSizeFromResource(Resources res, int resId, int reqWidth, int reqHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res ,resId, options);

        //Calc samplesize
        options.inSampleSize = calculateInSampleSize(options,reqHeight,reqHeight);
        return BitmapFactory.decodeResource(res,resId,options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if(height>reqHeight || width>reqWidth){
            final int halfHeight = height/2;
            final int halfWidth = width/2;

            while((halfHeight/inSampleSize)>reqHeight && (halfWidth/inSampleSize)>reqHeight){
                inSampleSize *=2;
            }
        }
        return inSampleSize;
    }

}
