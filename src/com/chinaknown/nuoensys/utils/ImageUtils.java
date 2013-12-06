package com.chinaknown.nuoensys.utils;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.ThumbnailUtils;
import android.util.Log;

public class ImageUtils {
//	private Context context = null;
//	public ImageUtils(Context context) {
//		this.context = context;
//	}
	
	
	public Bitmap decodeImage2(File file) {
		BitmapFactory.Options ops = new BitmapFactory.Options();
		ops.inPreferredConfig = Bitmap.Config.RGB_565;
		ops.inSampleSize = 4;
		ops.inPurgeable = true;
		ops.inInputShareable = true;
		try {
			return BitmapFactory.decodeStream(new FileInputStream(file), null, ops);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	//生成缩略图
	public static Bitmap decodeImage(InputStream is, int bitmapMaxWidth) {
		BitmapFactory.Options ops = new BitmapFactory.Options();
		ops.inJustDecodeBounds = true;
		Bitmap bm = BitmapFactory.decodeStream(is);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.JPEG, 70, baos);
		try {
//			bm = BitmapFactory.decodeStream(isBak,null,ops);
			bm = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length, ops);
		} catch (Exception e) {
			e.printStackTrace();
		}
		float realWidth = ops.outWidth;
		float realHeight = ops.outHeight;
//		System.out.println("真实图片高度：" + realHeight + "宽度:" + realWidth);
		Log.i("a", "真实图片高度：" + realHeight + "宽度:" + realWidth);
		int scale = (int) ((realHeight > realWidth ? realHeight : realWidth) / 100);
		if (scale <= 0) {
			scale = 1;
		}
		ops.inSampleSize = scale;
		ops.inJustDecodeBounds = false;

		try {
			bm = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length, ops);
			bm = ThumbnailUtils.extractThumbnail(bm, bitmapMaxWidth, bitmapMaxWidth, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
			return bm;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bm;
	}
	
	

	
	private Bitmap drawableToBitmap(LayerDrawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;

	}
}
