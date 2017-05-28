package com.example.oliverasker.skywarnmarkii.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.VideoView;

/**
 * Created by oliverasker on 5/18/17.
 */

public class VideoUtility {
    private static final String TAG = "VideoUtility";

    public static void createVideoThumbnail(Uri videoUri, VideoView videoView, Context mContext) {
        final Bitmap thumb = ThumbnailUtils.createVideoThumbnail(BitmapUtility.getRealPathFromURI(videoUri, mContext),
                MediaStore.Images.Thumbnails.MINI_KIND);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
        videoView.setBackground(bitmapDrawable);

    }

    public static BitmapDrawable getVideoThumbnailBitmap(Uri videoUri, VideoView videoView, Context mContext) {
        final Bitmap thumb = ThumbnailUtils.createVideoThumbnail(BitmapUtility.getRealPathFromURI(videoUri, mContext),
                MediaStore.Images.Thumbnails.MINI_KIND);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
        return bitmapDrawable;
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();

            if (Build.VERSION.SDK_INT >= 14 & videoPath != null)
//                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
//            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }


}
