package com.joysoft.andutils.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.provider.MediaStore.Images;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.ImageView;

import com.joysoft.andutils.app.DeviceUtils;
import com.joysoft.andutils.lg.Lg;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Bitmap工具类
 * @author fengmiao
 *
 */
public class BitmapUtils {

	/**
	 * 使用TransitionDrawable实现图片显示的时候有渐变效果
	 * @param imageView
	 * @param bitmap
	 */
	private void setImageBitmap(Context mContext,ImageView imageView, Bitmap bitmap) {
		// Use TransitionDrawable to fade in.
		final TransitionDrawable td = new TransitionDrawable(new Drawable[] { new ColorDrawable(mContext.getResources().getColor(android.R.color.transparent)), new BitmapDrawable(mContext.getResources(), bitmap) });
		//noinspection deprecation
		imageView.setBackgroundDrawable(imageView.getDrawable());
		imageView.setImageDrawable(td);
		td.startTransition(200);
	}
	
	/**
	 * 获取图片规格:宽、高等
	 * @param absPath
	 * @return
	 */
	public static ImageSize getImageSize(String absPath) {
		if (TextUtils.isEmpty(absPath)) {
			return null;
		}
		Options options = getOptions(absPath);
		int width = options.outWidth;
		int height = options.outHeight;
		ImageSize size = new ImageSize(width, height);
		return size;
	}
	
	/**
	 * 获取当前bitmap字节数
	 * @param bitmap
	 * @return
	 */
	@TargetApi(VERSION_CODES.HONEYCOMB_MR1)
	public static int getImageBytes(Bitmap bitmap) {
		if (bitmap == null) {
			return 0;
		}
		int bitmapSize = 0;
		if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1) {
			bitmapSize = bitmap.getByteCount();
		} else {
			bitmapSize = bitmap.getRowBytes() * bitmap.getHeight();
		}
		if (bitmapSize < 0) {
			bitmapSize = 0;
		}

		return bitmapSize;
	}
	
	public static Options getOptions(String absPath) {
		if (TextUtils.isEmpty(absPath)) {
			return null;
		}
		Options options = new Options();
		options.inPreferredConfig = Config.ALPHA_8;
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(absPath, options);
		return options;
	}
	
	
	/**
	 * ?按比例压缩图片Options
	 * @param absPath
	 * @param desireWidth
	 * @param desireHeight
	 * @return
	 */
	public static Options getOptions(String absPath, int desireWidth,
			int desireHeight) {
		if (!FileUtils.exists(absPath) || desireWidth <= 0 || desireHeight <= 0) {
			return null;
		}
		Options options = getOptions(absPath);
		int scaleW = sampleSize(options.outWidth, desireWidth);
		int scaleH = sampleSize(options.outHeight, desireWidth);
		options.inSampleSize = scaleH > scaleW ? scaleH : scaleW;
		options.inJustDecodeBounds = false;
		return options;
	}
	
	
	/**
	 * 获取asset文件夹下图片
	 * @param context
	 * @param filePath
	 * @return
	 */
	public static Bitmap getAssetBitmap(Context context, String filePath) {
		AssetManager assetManager = context.getAssets();
		InputStream ips = null;
		Bitmap bitmap = null;
		try {
			ips = assetManager.open(filePath);
			bitmap = BitmapFactory.decodeStream(ips);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	/**
	 * 根据路径获取图片
	 * @param absPath
	 * @return
	 */
	public static Bitmap getImage(String absPath) {
		if (!FileUtils.exists(absPath)) {
			Lg.d("invalid file path: " + absPath);
			return null;
		}

		Lg.d("decode bitmap " + absPath);
		long enter = System.currentTimeMillis();
		Bitmap bitmap = BitmapFactory.decodeFile(absPath);
		long delta = System.currentTimeMillis() - enter;
		Lg.d("decode bitmap time " + delta);

		return bitmap;
	}
	
	
	/**
	 * 获取指定大小的图片
	 * @param absPath
	 * @param size
	 * @return
	 */
	public static Bitmap getImage(String absPath, ImageSize size) {
		if (TextUtils.isEmpty(absPath)) {
			return null;
		}

		if (ImageSize.valid(size)) {
			return getImage(absPath, size.getWidth(), size.getHeight());
		} else {
			return getImage(absPath);
		}
	}
	
	/**
	 * 获取指定大小的图片
	 * @param absPath
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getImage(String absPath, int width, int height) {
		if (width <= 0 && height <= 0 || !FileUtils.exists(absPath)) {
			Lg.e("invalid parameters absPath " + absPath + " width "
					+ width + " height " + height);
			return null;
		}

		Lg.d("decode bitmap " + absPath + " width " + width
				+ " height " + height);
		long enter = System.currentTimeMillis();
		Options options = getOptions(absPath, width, height);
		Bitmap bitmap = BitmapFactory.decodeFile(absPath, options);
		if (bitmap == null) {
			Lg.d("decode bitmap failed " + absPath);
			return null;
		}

		int rotate = getRotate(absPath);
		bitmap = rotateImage(bitmap, rotate);
		System.gc();
		long delta = System.currentTimeMillis() - enter;
		Lg.v("decode bitmap time " + delta);
		return bitmap;
	}
	
	
	/**
	 * 旋转Bitmp
	 * @param bitmap
	 * @param rotate
	 * @return
	 */
	public static Bitmap rotateImage(Bitmap bitmap, int rotate) {
		if (bitmap == null) {
			return bitmap;
		}
		Matrix matrix = new Matrix();
//		matrix.setRotate(rotate);
		matrix.postRotate(rotate);
		Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		if (rotated != null && !rotated.equals(bitmap)) {
			bitmap.recycle();
			bitmap = rotated;
		}
		return bitmap;
	}
	
	/**
	 * 保存图片
	 * @param bitmap
	 * @param absPath
	 * @return
	 */
	public static boolean saveImage(Bitmap bitmap, String absPath) {
		return saveBitmap(bitmap, absPath, 100);
	}
	
	/**
	 * 根据指定质量保存图片
	 * @param bitmap
	 * @param absPath  要保存的路径
	 * @param quality
	 * @return
	 */
	public static boolean saveBitmap(Bitmap bitmap, String absPath, int quality) {
		if (!FileUtils.create(absPath)) {
			Lg.d("create file failed.");
			return false;
		}

		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			File outFile = new File(absPath);
			fos = new FileOutputStream(outFile);
			bos = new BufferedOutputStream(fos);
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
		} catch (Exception e) {
			e.printStackTrace();
			Lg.e("failed to write image content");
			return false;
		}finally {
			try {
				if (bos != null) {
					fos.flush();
					fos.close();
					bos.flush();
					bos.close();
					bitmap.recycle();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return true;
	}
	
	/**
	 * 获取正方形图片
	 * @param bitmap
	 * @return
	 */
	public static Bitmap squareImage(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		if (width == height) {
			Lg.d("no need to square image");
			return bitmap;
		}
		int x = (height < width) ? ((width - height) / 2) : 0;
		int y = (width < height) ? ((height - width) / 2) : 0;
		int pixels = width < height ? width : height;
		Bitmap square = Bitmap.createBitmap(bitmap, x, y, pixels, pixels);
		return square;
	}
	
	/**
	 * 获取图片旋转角度
	 * @param absPath
	 * @return
	 */
	public static int getRotate(String absPath) {
		if (!FileUtils.exists(absPath)) {
			Lg.w("invalid file path");
			return 0;
		}

		ExifInterface exifInterface = null;
		try {
			exifInterface = new ExifInterface(absPath);
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}

		int orientation = exifInterface.getAttributeInt(
				ExifInterface.TAG_ORIENTATION,
				ExifInterface.ORIENTATION_UNDEFINED);
		int rotate = 0;
		switch (orientation) {
		case ExifInterface.ORIENTATION_ROTATE_90:
			rotate = 90;
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			rotate = 180;
			break;
		case ExifInterface.ORIENTATION_ROTATE_270:
			rotate = 270;
			break;
		default:
			break;
		}
		Lg.v("image rotate " + rotate);
		return rotate;
	}
	
	/**
	 * 调整bitmap大小
	 * @param bitmap
	 * @param desireWidth 期望宽度
	 * @param desireHeight  期望高度
	 * @param isEnlarge  是否期望图片变大 true--变大
	 * @return
	 */
	public static Bitmap resizeImage(Bitmap bitmap, int desireWidth,
			int desireHeight, boolean isEnlarge) {
		if (bitmap == null || desireHeight <= 0 || desireWidth <= 0) {
			return null;
		}

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		if (width <= 0 || height <= 0) {
			return bitmap;
		}
		if (!isEnlarge && (width < desireWidth && height < desireHeight)) {
			return bitmap;
		}
		float scale;
		if (width < height) {
			scale = (float) desireHeight / (float) height;
			if (desireWidth < width * scale) {
				scale = (float) desireWidth / (float) width;
			}
		} else {
			scale = (float) desireWidth / (float) width;
		}

		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

		return bitmap;
	}
	
	
	/**
	 * 获取图片缩略图
	 * @param absPath
	 * @param width
	 * @param height
	 * @return
	 */
	public final static Bitmap getThumbnail(String absPath, int width,
			int height) {
		if (!FileUtils.exists(absPath) || width <= 0 || height <= 0) {
			return null;
		}
		Options options = getOptions(absPath, width, height);
		try {
			Bitmap bitmap = BitmapFactory.decodeFile(absPath, options);
			Bitmap result = ThumbnailUtils.extractThumbnail(bitmap, width,
					height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
			// recycle(bitmap);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ? 获取视频缩略图
	 * @param absPath
	 * @param width
	 * @param height
	 * @return
	 */
	public final static Bitmap getVideoThumbnail(String absPath, int width,
			int height) {
		if (!FileUtils.exists(absPath) || width <= 0 || height <= 0) {
			return null;
		}
		Bitmap bitmap = null;
		bitmap = ThumbnailUtils.createVideoThumbnail(absPath,
				Images.Thumbnails.MINI_KIND);
		Lg.d("bitmap width " + bitmap.getWidth() + " height "
				+ bitmap.getHeight());
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}
	
	/**
	 * 添加水印
	 * @param context
	 * @param bitmap
	 * @param waterResId
	 * @param gravity
	 * @return
	 */
	public final static Bitmap waterMark(Context context, Bitmap bitmap,
			int waterResId, int gravity) {
		Drawable waterPrintDrawable = context.getResources().getDrawable(
				waterResId);
		int waterPrintWidth = waterPrintDrawable.getIntrinsicWidth();
		int waterPrintHeight = waterPrintDrawable.getIntrinsicHeight();
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		if (gravity == Gravity.CENTER) {
			waterPrintDrawable.setBounds((w - waterPrintWidth) / 2,
					(h - waterPrintHeight) / 2, (w + waterPrintWidth) / 2,
					(h + waterPrintHeight) / 2);
		} else {
			waterPrintDrawable.setBounds((w - waterPrintWidth) / 2, h,
					(w + waterPrintWidth) / 2, h + waterPrintHeight);
		}
		Canvas mCanvas = new Canvas(bitmap);
		waterPrintDrawable.draw(mCanvas);
		return bitmap;
	}
	
	/**
	 * 获取圆形图片
	 * @param context
	 * @param bitmap
	 * @param borderDips ? 距边缘距离
	 * @param desireWidth  直径
	 * @return
	 */
	public final static Bitmap toRounded(Context context, Bitmap bitmap,
			 int borderDips, int desireWidth) {
		try {
			Bitmap bitmap0 = squareImage(bitmap);
			Bitmap bitmap1 = resizeImage(bitmap0, desireWidth, desireWidth,
					true);
			int toPX = DeviceUtils.dip2px(context, borderDips);
			int maxBorder = (desireWidth / 2) / 5;
			maxBorder = maxBorder > 15 ? 15 : maxBorder;
			final int borderSizePx = toPX > maxBorder ? maxBorder : toPX;

			final int size = desireWidth;
			int center = (int) (size / 2);
			int left = (int) ((desireWidth - size) / 2);
			int top = (int) ((desireWidth - size) / 2);
			int right = left + size;
			int bottom = top + size;

			Bitmap output = Bitmap.createBitmap(size, size, Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final Paint paint = new Paint();

			final Rect src = new Rect(left, top, right, bottom);
			final Rect dst = new Rect(0, 0, size, size);

			canvas.drawARGB(0, 0, 0, 0);
			// draw border
			paint.setColor(Color.WHITE);
			paint.setAntiAlias(true);
			int radius = center - borderSizePx;
			canvas.drawCircle(center, center, radius, paint);
			// draw bitmap
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap1, src, dst, paint);
			paint.setXfermode(null);

			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth((float) borderSizePx);
			radius = center - borderSizePx / 2;
			canvas.drawCircle(center, center, radius, paint);
			canvas.save();
			recycle(bitmap0, bitmap1, bitmap);
			return output;
		} catch (Exception e) {
			Lg.e("Exception >>>>  "+e.toString());
			e.printStackTrace();
		}
		return bitmap;
	}
	
	/**
	 * 获取圆角图片
	 * @param context
	 * @param bitmap
	 * @return
	 */
	public static Bitmap toRoundCorner(Context context, Bitmap bitmap) {
		try {
			int w = bitmap.getWidth();
			int h = bitmap.getHeight();
			final int delta = DeviceUtils.dip2px(context,5);
			final float roundPx = DeviceUtils.dip2px(context,14);
			final Paint paint = new Paint();
			paint.setAntiAlias(true);

			/* draw round foreground */
			int foreW = w - 2 * delta;
			int foreH = h - 2 * delta;
			Bitmap foreBmp = Bitmap
					.createBitmap(foreW, foreH, Config.ARGB_8888);
			Rect rect = new Rect(0, 0, foreW, foreH);
			RectF rectF = new RectF(rect);
			Canvas canvas0 = new Canvas(foreBmp);
			canvas0.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas0.drawBitmap(bitmap, null, rect, paint);

			/* draw round background */
			Drawable drawable = getDrawbale(0xffffffff, (int) (delta + roundPx));
			drawable.setBounds(0, 0, w, h);
			Bitmap result = Bitmap.createBitmap(w, h, Config.ARGB_8888);
			Canvas canvas = new Canvas(result);
			drawable.draw(canvas);
			canvas.drawBitmap(foreBmp, delta, delta, null);
			recycle(foreBmp, bitmap);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return bitmap;
		}
	}
	
	/**
	 * ?渐变色
	 * @param argb
	 * @param radius
	 * @return
	 */
	public final static GradientDrawable getDrawbale(int argb, int radius) {
		GradientDrawable drawable = new GradientDrawable();
		drawable.setCornerRadius(radius);
		drawable.setColor(argb);
		return drawable;
	}
	
	
	/**
	 * 在原来的图片后面画一个圆形,并把这个图片放在中间
	 * @param bitmap
	 * @param deltaRadius 
	 * @param color
	 * @return
	 */
	public final static Bitmap drawRing(Bitmap bitmap, int deltaRadius,
			int color) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int dia = deltaRadius * 2 + w;
		float radius = (float) dia / 2;
		Bitmap resultBitmap = Bitmap.createBitmap(dia, dia, Config.ARGB_8888);
		Paint paint = new Paint();
		paint.setColor(color);
		paint.setAlpha(128);
		paint.setAntiAlias(true);
		Canvas canvas = new Canvas(resultBitmap);
		canvas.drawCircle(radius, radius, radius, paint);
		canvas.drawBitmap(bitmap, deltaRadius, (dia - h)/2, null);
		return resultBitmap;
	}
	
	
	/**
	 * 画一个圆形
	 * @param argb  圆形的背景颜色
	 * @param dia  直径
	 * @return
	 */
	public final static Bitmap getRound(int argb, int dia) {
		float radius = dia / 2;
		Bitmap resultBitmap = Bitmap.createBitmap(dia, dia, Config.ARGB_8888);
		Paint paint = new Paint();
		paint.setColor(argb);
		paint.setAntiAlias(true);
		Canvas canvas = new Canvas(resultBitmap);
		canvas.drawCircle(radius, radius, radius, paint);
		return resultBitmap;
	}

	
	/**
	 * 图片拉伸 以图片底边为起点进行拉伸
	 * @param bitmap
	 * @param height
	 * @return
	 */
	public final static Bitmap stretch(Bitmap bitmap, int height) {
		if (bitmap == null) {
			return null;
		}
		if (height <= bitmap.getHeight()) {
			return bitmap;
		}
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int tmpHeight = 1;
		Bitmap resultBitmap = Bitmap.createBitmap(w, height, Config.ARGB_8888);
		Canvas mCanvas = new Canvas(resultBitmap);
		mCanvas.drawBitmap(bitmap, 0, 0, null);
		Bitmap tmp = Bitmap
				.createBitmap(bitmap, 0, h - tmpHeight, w, tmpHeight);
		bitmap.recycle();
		for (int index = 0; index < (height - h) / tmpHeight; index++) {
			mCanvas.drawBitmap(tmp, 0, h + index * tmpHeight, null);
		}
		tmp.recycle();
		return resultBitmap;
	}

	/**
	 * 旋转360°后再覆盖一张图片
	 * @param bitmap
	 * @return
	 */
	public static Bitmap overlay(final Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		if (width == 0 || height == 0) {
			return null;
		}
		float rota = 345;
		double radians = Math.toRadians(rota);
		/* after rotate , the new width is the original width */
		int rotateWidth = (int) (width / (Math.abs(Math.sin(radians)) + Math
				.abs(Math.cos(radians))));
		int delta = (width - rotateWidth) / 2;

		// result
		Bitmap result = Bitmap.createBitmap(width, width, Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		// foreground
		Bitmap foreBmp = resizeImage(bitmap, rotateWidth, rotateWidth, true);
		// rotate
		Matrix matrix = new Matrix();
		matrix.postRotate(rota);
		
		Bitmap rotate = Bitmap.createBitmap(foreBmp, 0, 0, foreBmp.getWidth(),
				foreBmp.getHeight(), matrix, true);
		
//		Bitmap rotate = Bitmap.createBitmap(foreBmp, 0, 0, rotateWidth,
//				rotateWidth, matrix, true);
		
		canvas.drawBitmap(rotate, 0, 0, null);
		canvas.drawBitmap(foreBmp, delta, delta, null);
		recycle(bitmap, rotate, foreBmp);
		return result;
	}

	/**
	 * 图片下方阴影
	 * @param bitmap
	 * @return
	 */
	public static Bitmap drawLayoutDropShadow(Bitmap bitmap) {
		BlurMaskFilter blurFilter = new BlurMaskFilter(2,
				BlurMaskFilter.Blur.OUTER);
		Paint shadowPaint = new Paint();
		shadowPaint.setMaskFilter(blurFilter);
		int[] offsetXY = { 5, 5 };
		Bitmap shadowBitmap = bitmap.extractAlpha(shadowPaint, offsetXY);
		Bitmap shadowImage32 = shadowBitmap.copy(Config.ARGB_8888, true);
		Canvas c = new Canvas(shadowImage32);
		c.drawBitmap(bitmap, 0, 0, null);
		return shadowImage32;
	}

	/**
	 * 图片模糊
	 * @param bmp
	 * @return
	 */
	public static Bitmap blur(Bitmap bmp) {
		int iterations = 1;
		int radius = 8;
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		int[] inPixels = new int[width * height];
		int[] outPixels = new int[width * height];
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Config.ARGB_8888);
		bmp.getPixels(inPixels, 0, width, 0, 0, width, height);
		for (int i = 0; i < iterations; i++) {
			blur(inPixels, outPixels, width, height, radius);
			blur(outPixels, inPixels, height, width, radius);
		}
		bitmap.setPixels(inPixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	private static void blur(int[] in, int[] out, int width, int height,
			int radius) {
		int widthMinus1 = width - 1;
		int tableSize = 2 * radius + 1;
		int divide[] = new int[256 * tableSize];

		for (int index = 0; index < 256 * tableSize; index++) {
			divide[index] = index / tableSize;
		}

		int inIndex = 0;

		for (int y = 0; y < height; y++) {
			int outIndex = y;
			int ta = 0, tr = 0, tg = 0, tb = 0;

			for (int i = -radius; i <= radius; i++) {
				int rgb = in[inIndex + clamp(i, 0, width - 1)];
				ta += (rgb >> 24) & 0xff;
				tr += (rgb >> 16) & 0xff;
				tg += (rgb >> 8) & 0xff;
				tb += rgb & 0xff;
			}

			for (int x = 0; x < width; x++) {
				out[outIndex] = (divide[ta] << 24) | (divide[tr] << 16)
						| (divide[tg] << 8) | divide[tb];

				int i1 = x + radius + 1;
				if (i1 > widthMinus1)
					i1 = widthMinus1;
				int i2 = x - radius;
				if (i2 < 0)
					i2 = 0;
				int rgb1 = in[inIndex + i1];
				int rgb2 = in[inIndex + i2];

				ta += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
				tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
				tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
				tb += (rgb1 & 0xff) - (rgb2 & 0xff);
				outIndex += height;
			}
			inIndex += width;
		}
	}

	private static int clamp(int x, int a, int b) {
		return (x < a) ? a : (x > b) ? b : x;
	}

	private static int sampleSize(int size, int target) {
		int sample = 1;
		for (int index = 0; index < 10; index++) {
			if (size < target * 2) {
				break;
			}
			size = size / 2;
			sample = sample * 2;
		}
		return sample;
	}

	private final static void recycle(Bitmap... bitmaps) {
		if (bitmaps == null || bitmaps.length == 0) {
			return;
		}
		for (Bitmap bitmap : bitmaps) {
			if (bitmap == null) {
				continue;
			}
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
				continue;
			}
			bitmap = null;
		}
		System.gc();
	}
}