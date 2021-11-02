package com.example.dell.databindingapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.example.dell.databindingapp.base.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ImageUtil {
    private static final String TAG = "ImageUtil";

    /**
     * 获取图片文件宽高
     *
     * @param path
     * @param size
     * @return
     */
    public static void obtainImageSize(String path, int[] size) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        if (size == null || size.length < 2) {
            size = new int[2];
        }
        if (TextUtils.isEmpty(path)) {
            return;
        }
        BitmapFactory.decodeFile(path, options);
        int orientation = getImageOrientation(path);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
            case ExifInterface.ORIENTATION_ROTATE_270: {
                size[0] = options.outHeight;
                size[1] = options.outWidth;
                break;
            }
            default: {
                size[0] = options.outWidth;
                size[1] = options.outHeight;
                break;
            }
        }
    }

    public static int getImageOrientation(String imageLocalPath) {
        try {
            ExifInterface exifInterface = new ExifInterface(imageLocalPath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            return orientation;
        } catch (IOException e) {
            e.printStackTrace();
            return ExifInterface.ORIENTATION_NORMAL;
        }
    }

    /**
     * 获取图片文件宽高
     *
     * @param imageFile
     * @param size
     * @return
     */
    public static void obtainImageSize(File imageFile, int[] size) {
        obtainImageSize(imageFile.getAbsolutePath(), size);
    }

    /**
     * 获取图片文件宽高
     *
     * @param resId
     * @param size
     * @return
     */
    public static void obtainImageSize(int resId, int[] size) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        if (size == null || size.length < 2) {
            size = new int[2];
        }
        BitmapFactory.decodeResource(App.Companion.instance().getResources(), resId, options);
        size[0] = options.outWidth;
        size[1] = options.outHeight;
    }

    /**
     * 获取图片的旋转角度
     *
     * @param filePath
     * @return
     */
    public static int getRotateAngle(String filePath) {
        int rotate_angle = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate_angle = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate_angle = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate_angle = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate_angle;
    }

    /**
     * 旋转图片角度
     *
     * @param angle
     * @param bitmap
     * @return
     */
    public static Bitmap setRotateAngle(int angle, Bitmap bitmap) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(angle);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return null;
    }

    //转换为圆形状的bitmap
    public static Bitmap createCircleImage(Bitmap source) {
        int length = source.getWidth() < source.getHeight() ? source.getWidth() : source.getHeight();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(length, length, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(length / 2, length / 2, length / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    /**
     * 图片压缩-质量压缩
     *
     * @param uri     源图片uri
     * @param quality 压缩比例0-100
     * @return 压缩后的路径
     */
    public static String compressImage(Uri uri, int quality) {
        Context context =App.Companion.instance();
        //转成图片路径
        String oldPath = FileUtil.getPath(context, uri);
        FileInputStream in;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !Environment.isExternalStorageLegacy()) {
                in = (FileInputStream) context.getContentResolver().openInputStream(uri);
            } else {
                in = new FileInputStream(oldPath);
            }
            //创建临时文件
            File tempFile = KotlinUtilKt.newTempFile("temp_", FileUtil.getFileExtension(oldPath), context.getExternalFilesDir("image"));
            FileOutputStream out = new FileOutputStream(tempFile);
            //复制文件
            KotlinUtilKt.copyFile(in, out);
            //鲁班压缩
            String targetPath = KotlinUtilKt.luBanToPath(tempFile.getPath());
//            Bitmap bm = getSmallBitmap(targetPath);//获取一定尺寸的图片
//            int degree = getRotateAngle(targetPath);//获取相片拍摄角度
//            if (degree != 0) {//旋转照片角度，防止头像横着显示
//                bm = setRotateAngle(degree, bm);
//            }
//            //执行压缩
//            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
//            out.close();
            return targetPath;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "compressImage>error" + e.getMessage());
        }
        return "";
    }

    /**
     * GIF压缩-质量压缩
     *
     * @param uri     源图片uri
     * @return 压缩后的路径
     */
    public static String compressGif(Uri uri,int[] imgSize ) {
        Context context = App.Companion.instance();
        //转成图片路径
        String oldPath = FileUtil.getPath(context, uri);
        try {
            Bitmap bitmap = Glide.with(context).asBitmap().load(uri).submit().get();
            imgSize[0]=bitmap.getWidth();
            imgSize[1]=bitmap.getHeight();
            GifDrawable gifDrawable = Glide.with(context).asGif()
                    .load(uri)
                    .thumbnail(0.5f)
                    .encodeQuality(20)
                    .override(34, 62)
                    .into(34, 62)
                    .get();
            int size = gifDrawable.getSize();
//            FileInputStream  in = new FileInputStream(file);
//            //创建临时文件
//            File targetPath = KotlinUtilKt.newTempFile("temp_", FileUtil.getFileExtension(oldPath), context.getExternalFilesDir("image"));
//            FileOutputStream out = new FileOutputStream(targetPath);
//            //复制文件
//            KotlinUtilKt.copyFile(in, out);

            ByteBuffer buffer = gifDrawable.getBuffer();
            Log.d("Felix","[compressGif] file: "+new File(oldPath).length());
            String targetPath = saveBuffer2Local(context,buffer,oldPath);
            Log.d("Felix","[compressGif] targetPath: "+targetPath.length());
            Log.d("Felix","[compressGif] targetPath: "+targetPath);
            Log.d("Felix","[compressGif] size: "+size);

            return targetPath;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "compressGif -> error",e);
        }
        return "";
    }


    /**
     * 保存gif。使用nio写入
     *
     * @param context
     * @param byteBuffer
     * @param oldPath
     * @return
     */
    public static String saveBuffer2Local(Context context, ByteBuffer byteBuffer, String oldPath) throws FileNotFoundException {
        // 声明文件对象
        File file = null;
        // 声明输出流
        FileOutputStream outStream = null;
        FileChannel channel = null;
        try {
            //创建临时文件
            file = KotlinUtilKt.newTempFile("temp_", FileUtil.getFileExtension(oldPath), context.getExternalFilesDir("image"));

            // 获得输出流，如果文件中有内容，追加内容
            outStream = new FileOutputStream(file);
            channel = outStream.getChannel();
            //不能确定channel.write()能一次性写入buffer的所有数据
            //所以通过判断是否有余留循环写入
            while (byteBuffer.hasRemaining()) {
                channel.write(byteBuffer);
            }
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
                if (channel != null) {
                    channel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (file != null) {
            return file.getPath();
        }
        return "";
    }

    /**
     * 图片压缩-质量压缩
     *
     * @param uri 源图片uri
     * @return 压缩后的路径
     */
    public static String compressImage(Uri uri) {
        return compressImage(uri, 50);
    }

    /**
     * 图片压缩-质量压缩
     *
     * @param filePath 源图片路径
     * @param quality  压缩比例0-100
     * @return 压缩后的路径
     */
    public static String compressImage(String filePath, int quality) {
        Context context = App.Companion.instance();
        //压缩临时文件路径
        File outputFile = KotlinUtilKt.newTempFile("temp_", FileUtil.getFileExtension(filePath), context.getExternalFilesDir("image"));
        Bitmap bm = getSmallBitmap(filePath);//获取一定尺寸的图片
        int degree = getRotateAngle(filePath);//获取相片拍摄角度
        if (degree != 0) {//旋转照片角度，防止头像横着显示
            bm = setRotateAngle(degree, bm);
        }
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "compressImage>error" + e.getMessage());
            return filePath;
        }
        return outputFile.getPath();
    }

    /**
     * 图片压缩-质量压缩
     *
     * @param filePath 源图片路径
     * @return 压缩后的路径
     */
    public static String compressImage(String filePath) {
        return compressImage(filePath, 50);
    }

    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
        BitmapFactory.decodeFile(filePath, options);
        // 计算缩放比
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (reqWidth > 0 && reqHeight > 0) {
            if (height > reqHeight || width > reqWidth) {
                final int heightRatio = Math.round((float) height / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);
                inSampleSize = Math.min(heightRatio, widthRatio);
            }
        }
        return inSampleSize;
    }
}
