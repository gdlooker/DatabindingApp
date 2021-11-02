package com.example.dell.databindingapp.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.loader.content.CursorLoader;

import com.example.dell.databindingapp.base.App;
import com.example.dell.databindingapp.base.data.BaseData;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WuShengjun on 2017/11/3.
 */

public class FileUtil {
    private static final String TAG = "FileUtil";
    private final Context mContext;

    public FileUtil(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) { // MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) { // MediaStore (and general)
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) { // File
            return uri.getPath();
        }

        return "";
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int columnIndex = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(columnIndex);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param file            将要删除的文件或目录
     * @param deleteDirectory 是否删除文件夹
     * @return
     */
    public static boolean deleteFile(File file, boolean deleteDirectory) {
        if (file == null || !file.exists())
            return false;
        if (file.isDirectory()) { // 若为目录
            File[] childFiles = file.listFiles(); // 遍历里面所有文件和目录
            // 递归删除目录中的子目录下
            if (childFiles != null) {
                for (int i = 0; i < childFiles.length; i++) {
//                    MyLg.e("directoryFile", "childFilePath=" + childFiles[i].getName());
                    boolean succ = deleteFile(childFiles[i], deleteDirectory);
//                    if (!succ)
//                        return false;
                }
            }
        }

        if (!file.isDirectory() || deleteDirectory) {
            return file.delete();
        }
        // file为文件或目录此时为空，可以删除
//        MyLg.e("deleteFile", "filePath=" + file.getName());
        return true;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param filePath        将要删除的文件路径
     * @param deleteDirectory 是否删除文件夹
     * @return
     */
    public static boolean deleteFile(String filePath, boolean deleteDirectory) {
        return deleteFile(new File(filePath), deleteDirectory);
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir
     *            要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName
     *            要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteSingleFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param filePath 将要删除的文件或目录路径
     * @return
     */
    public static boolean deleteFile(String filePath) {
        return deleteFile(filePath, true);
    }

    /**
     * 判断SDcard是否可用
     *
     * @return
     */
    public static boolean SDCardExists() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 判断外置sd卡是否挂载
     */
    public static boolean isStorageMounted(Context context) {
        boolean isMounted = false;
        StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Method getState = storageVolumeClazz.getMethod("getState");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                String state = (String) getState.invoke(storageVolumeElement);
                if (removable && state.equals(Environment.MEDIA_MOUNTED)) {
                    isMounted = true;
                    break;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return isMounted;
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName
     * @return
     */
    public static boolean isFileExists(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return false;
        }
        File file = new File(fileName);
        return file.exists();
    }

    /**
     * 判断文件是否存在
     *
     * @param name
     * @param dir
     * @return
     */
    public static boolean isFileExists(String dir, String name) {
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(dir, name);

        return file.exists();
    }

    /**
     * 判断文件是否存在
     *
     * @param name
     * @param dir
     * @return
     */
    public static URI getFileUri(String dir, String name) {
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(dir, name);

        if (file.exists()) {
            return file.toURI();
        }
        return null;
    }

    /**
     * 获取SDcard的总存储量,返回-1则不存在
     *
     * @return
     */
    public static long getSDCardTotalSpace() {
        File file = Environment.getExternalStorageDirectory();
        return getFileTotalSpace(file);
    }

    /**
     * 获取SDcard的剩余存储量,返回-1则不存在
     *
     * @return
     */
    public static long getSDCardUsableSpace() {
        File file = Environment.getExternalStorageDirectory();
        return getFileUsableSpace(file);
    }

    /**
     * 获取文件夹总空间，-1为不存在
     *
     * @param file
     * @return
     */
    public static long getFileTotalSpace(File file) {
        if (file != null && file.exists()) {
            return file.getTotalSpace(); // 文件的总大小（此方法应用于8以上，需要在此方法打上NewApi的注解）
        } else {
            return -1;
        }
    }

    /**
     * 获取文件夹剩余空间，-1为不存在
     *
     * @return
     */
    public static long getFileUsableSpace(File file) {
        if (file != null && file.exists()) {
            return file.getUsableSpace(); // 文件的总大小（此方法应用于8以上，需要在此方法打上NewApi的注解）
        } else {
            return -1;
        }
    }

    /**
     * 获取文件的大小
     *
     * @param file
     * @return 返回字节数b
     */
    public static long getFilesSize(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        long size = 0L;
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles(); // 遍历里面所有文件和目录
            // 递归删除目录中的子目录下
            if (childFiles != null) {
                for (int i = 0; i < childFiles.length; i++) {
                    if (childFiles[i].isDirectory()) { // 如果是文件夹再递归
                        size += getFilesSize(childFiles[i]);
                    } else { // 否则就所有文件大小加起来
                        size += childFiles[i].length();
                    }
                }
            }
        } else {
            size += file.length(); // 把文件里所有文件（不是文件夹）大小加起来
        }
        return size;
    }

    /**
     * 获取文件大小
     *
     * @param filePath
     * @return
     */
    public static long getFilesSize(String filePath) {
        return getFilesSize(new File(filePath));
    }

    /**
     * 保存crash到文件
     *
     * @param ex
     * @param crashFilePath
     * @return crashMessage
     */
    public static String saveCrashFile(Context context, Throwable ex, String crashFilePath) {
        File file = new File(crashFilePath);
        return saveCrashFile(context, ex, file);
    }

    /**
     * 保存crash到文件
     *
     * @param ex
     * @param crashPathFile
     * @return crashMessage
     */
    public static String saveCrashFile(Context context, Throwable ex, File crashPathFile) {
        if (!crashPathFile.exists()) { // 创建文件夹
            crashPathFile.mkdirs();
        }
        StringBuffer sb = new StringBuffer();
        String saveTime = DateUtils.getCurrFormatDate(DateUtils.DATEFORMAT_FULL);
        sb.append("DateTime: " + saveTime + "\n");
        sb.append("DeviceInfo: " + Build.MANUFACTURER + " " + Build.MODEL + "\n");
        sb.append("AppVersion: " + getPackageInfo(context).versionName
                + "_" + getPackageInfo(context).versionCode + "\n");

        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();
        String result = writer.toString();
        sb.append("Excetpion: \n");
        sb.append(result);

        Log.e("CrashHandler", result); // 打印输出，方便开发调试

        // 记录异常到特定文件中
        File crashFile = new File(crashPathFile, "log_v" + getPackageInfo(context).versionName + "_" + getPackageInfo(context).versionCode + "(" + saveTime + ").txt");
        crashFile.setReadOnly();
        try {
            writer = new FileWriter(crashFile);
            writer.write(sb.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Log.e("saveToCrashFile", "" + e.getMessage());
        }
        return result;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (info == null) {
            info = new PackageInfo();
        }
        return info;
    }

    /**
     * 包含Android 10版本以上
     *
     * @param uri
     * @return
     */
    public static String getTempFilePath(Uri uri) {
        Context context = App.Companion.instance();
        try {
            //android 10以上没有权限了
            //把文件复制到自己换成路径上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !Environment.isExternalStorageLegacy()) {
                InputStream in = context.getContentResolver().openInputStream(uri);
                File tempFile = KotlinUtilKt.newTempFile("temp_", getFileExtension(getPath(context, uri)), context.getExternalFilesDir("image"));
                FileOutputStream out = new FileOutputStream(tempFile);
                //复制文件
                KotlinUtilKt.copyFile(in, out);
                return tempFile.getAbsolutePath();
            } else {
                return getPath(context, uri);
            }
        } catch (Exception e) {
            Log.i("getTempFilePath error", e.getMessage());
        }
        return "";
    }

    public static String getFileMD5IfExists(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File file = new File(path);
        if( file.exists()){
            return getFileMD5(file);
        }else{
            return null;
        }
    }

    public static String getFileMD5(String path) {
        return getFileMD5(new File(path));
    }

    /**
     * 获取文件MD5值
     *
     * @param uri
     * @return [0]文件路径，[1]文件MD5值
     */
    public static String[] getFileMD5(Uri uri) {
        String[] arr = new String[2];
        arr[0] = getTempFilePath(uri);

        if (!TextUtils.isEmpty(arr[0])) {
            arr[1] = getFileMD5(arr[0]);
        } else {
            arr[1] = "";
        }
        return arr;
    }

    public static String getFileMD5(File file) {
        try {
            byte[] hash;
            byte[] buffer = new byte[8192];
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(file);
            int len;
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            hash = md.digest();

            //对生成的16字节数组进行补零操作
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e("FileUtil>getFileMD5 1", e.getMessage());
        } catch (UnsupportedEncodingException e) {
            Log.e("FileUtil>getFileMD5 2", e.getMessage());
        } catch (IOException e) {
            Log.e("FileUtil>getFileMD5 3", e.getMessage());
            e.printStackTrace();
        }

        return "";
    }

    public static byte[] file2Bytes(File file) {
        int byte_size = 1024;
        byte[] b = new byte[byte_size];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
                    byte_size);
            for (int length; (length = fileInputStream.read(b)) != -1; ) {
                outputStream.write(b, 0, length);
            }
            fileInputStream.close();
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件夹中文件的MD5值
     *
     * @param file
     * @param listChild ;true递归子目录中的文件
     * @return
     */
    public static Map<String, String> getDirMD5(File file, boolean listChild) {
        if (!file.isDirectory()) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        String md5;
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory() && listChild) {
                map.putAll(getDirMD5(f, listChild));
            } else {
                md5 = getFileMD5(f);
                if (md5 != null) {
                    map.put(f.getPath(), md5);
                }
            }
        }
        return map;
    }

    /**
     * 获取文件扩展名
     *
     * @param path
     * @return
     */
    public static String getFileExtension(String path) {
        if (path == null) {
            return ".tmp";
        }
        int index = path.lastIndexOf(".");
        if (index > -1) {
            return path.substring(index);
        }
        return ".tmp";
    }

    /**
     * 获取文件扩展名
     *
     * @param file
     * @return
     */
    public static String getFileExtension(File file) {
        String path = file.getPath();
        return getFileExtension(path);
    }

    /**
     * 通过Uri获取路径
     *
     * @param uri
     * @return
     */
    public String getFilePathByUri(Uri uri) {
        // 以 file:// 开头的
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            return uri.getPath();
        }
        // 以/storage开头的也直接返回
        if (isOtherDocument(uri)) {
            return uri.getPath();
        }
        // 版本兼容的获取！
        String path = getFilePathByUri_BELOWAPI11(uri);
        if (path != null) {
            Log.d(TAG, "getFilePathByUri_BELOWAPI11获取到的路径为：" + path);
            return path;
        }
        path = getFilePathByUri_API11to18(uri);
        if (path != null) {
            Log.d(TAG, "getFilePathByUri_API11to18获取到的路径为：" + path);
            return path;
        }
        path = getFilePathByUri_API19(uri);
        Log.d(TAG, "getFilePathByUri_API19获取到的路径为：" + path);
        return path;
    }

    private String getFilePathByUri_BELOWAPI11(Uri uri) {
        // 以 content:// 开头的，比如 content://media/extenral/images/media/17766
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            String path = null;
            String[] projection = new String[]{MediaStore.Images.Media.DATA};
            Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (columnIndex > -1) {
                        path = cursor.getString(columnIndex);
                    }
                }
                cursor.close();
            }
            return path;
        }
        return null;
    }

    private String getFilePathByUri_API11to18(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        String result = null;
        CursorLoader cursorLoader = new CursorLoader(mContext, contentUri, projection, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
            cursor.close();
        }
        return result;
    }

    private String getFilePathByUri_API19(Uri uri) {
        // 4.4及之后的 是以 content:// 开头的，比如 content://com.android.providers.media.documents/document/image%3A235700
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(mContext, uri)) {
                if (isExternalStorageDocument(uri)) {
                    // ExternalStorageProvider
                    String docId = DocumentsContract.getDocumentId(uri);
                    String[] split = docId.split(":");
                    String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        if (split.length > 1) {
                            return Environment.getExternalStorageDirectory() + "/" + split[1];
                        } else {
                            return Environment.getExternalStorageDirectory() + "/";
                        }
                        // This is for checking SD Card
                    }
                } else if (isDownloadsDocument(uri)) {
                    //下载内容提供者时应当判断下载管理器是否被禁用
                    int stateCode = mContext.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
                    if (stateCode != 0 && stateCode != 1) {
                        return null;
                    }
                    String id = DocumentsContract.getDocumentId(uri);
                    // 如果出现这个RAW地址，我们则可以直接返回!
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:", "");
                    }
                    if (id.contains(":")) {
                        String[] tmp = id.split(":");
                        if (tmp.length > 1) {
                            id = tmp[1];
                        }
                    }
                    Uri contentUri = Uri.parse("content://downloads/public_downloads");
                    Log.d(TAG, "测试打印Uri: " + uri);
                    try {
                        contentUri = ContentUris.withAppendedId(contentUri, Long.parseLong(id));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String path = getDataColumn(contentUri, null, null);
                    if (path != null) return path;
                    // 兼容某些特殊情况下的文件管理器!
                    String fileName = getFileNameByUri(uri);
                    if (fileName != null) {
                        path = Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
                        return path;
                    }
                } else if (isMediaDocument(uri)) {
                    // MediaProvider
                    String docId = DocumentsContract.getDocumentId(uri);
                    String[] split = docId.split(":");
                    String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    String selection = "_id=?";
                    String[] selectionArgs = new String[]{split[1]};
                    return getDataColumn(contentUri, selection, selectionArgs);
                }
            }
        }
        return null;
    }

    private String getFileNameByUri(Uri uri) {
        String relativePath = getFileRelativePathByUri_API18(uri);
        if (relativePath == null) relativePath = "";
        final String[] projection = {
                MediaStore.MediaColumns.DISPLAY_NAME
        };
        try (Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                return relativePath + cursor.getString(index);
            }
        }
        return null;
    }

    private String getFileRelativePathByUri_API18(Uri uri) {
        final String[] projection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            projection = new String[]{
                    MediaStore.MediaColumns.RELATIVE_PATH
            };
            try (Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.RELATIVE_PATH);
                    return cursor.getString(index);
                }
            }
        }
        return null;
    }

    private String getDataColumn(Uri uri, String selection, String[] selectionArgs) {
        final String column = MediaStore.Images.Media.DATA;
        final String[] projection = {column};
        try (Cursor cursor = mContext.getContentResolver().query(uri, projection, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isOtherDocument(Uri uri) {
        // 以/storage开头的也直接返回
        if (uri != null && uri.getPath() != null) {
            String path = uri.getPath();
            if (path.startsWith("/storage")) {
                return true;
            }
            return path.startsWith("/external_files");
        }
        return false;
    }

    public static void saveHttpLogToFile(String fileName, String header, String message, boolean append, String deliver) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        try {
            File file = new File(getLogFile(), fileName);
            boolean exists = file.exists();
//            file.setReadOnly();
            FileWriter writer = new FileWriter(file, append);
            if (!exists) {
                writer.write(header + deliver + "--------------------------\n");
            }
            writer.write(message + deliver);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Log.e("saveHttpLogToFile", "" + e.getMessage());
        }
    }

    public static void saveErrorLogToFile(String log) {
        //抛异常收取完成
        BaseData.INSTANCE.getChatSocketStatus().postValue(BaseData.STATUS_COMPLETE);
        if (TextUtils.isEmpty(log)) {
            return;
        }
        try {
            File file = new File(getLogFile(), "log_error_" + DateUtils.getCurrFormatDate("yyyyMMdd") + ".txt");
            FileWriter writer = new FileWriter(file, true);
            writer.write("【" + DateUtils.getCurrFormatDate("yyyy-MM-dd HH:mm:ss") + "】 " + log + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Log.e("saveErrorLogToFile", "" + e.getMessage());
        }
    }

    public static void saveIMLogToFile(String log) {
        if (TextUtils.isEmpty(log)) {
            return;
        }
        try {
            File file = new File(getLogFile(), "log_im_" + DateUtils.getCurrFormatDate("yyyyMMdd") + ".txt");
            FileWriter writer = new FileWriter(file, true);
            writer.write("【" + DateUtils.getCurrFormatDate("yyyy-MM-dd HH:mm:ss") + "】 " + log + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Log.e("saveIMLogToFile", "" + e.getMessage());
        }
    }

    private static File getLogFile() {
        return App.Companion.instance().getFilesDir();
    }

    /**
     * 格式化显示文件大小:<br>
     * 1KB=1024B<br>
     * 1MB=1024KB<br>
     * 1GB=1024MB<br>
     * 1TB=1024GB<br>
     * 1PB=1024TB<br>
     * 1EB=1024PB<br>
     * 1ZB =1024EB<br>
     * 1YB =1024ZB<br>
     * 1BB=1024YB<br>
     *
     * @param size
     * @param precision 精度 0~6
     * @return
     */
    public static String sizeFormat(long size, int precision) {
        if(size<=0){
            return "0B";
        }
        if (precision > 6) {
            precision = 6;
        } else if (precision < 0) {
            precision = 0;
        }
        String format = "%." + precision + "f %s";
        Double val = 0.0;
        String unit = "B";
        long T = 1;
        if (size >= 1 && size < 1024 * 1024L) {
            // KB范围
            T = 1024L;
            unit = "KB";
        } else if (size < 1024 * 1024 * 1024L) {
            // MB 范围
            T = 1024 * 1024L;
            unit = "MB";
        } else if (size < 1024 * 1024 * 1024 * 1024L) {
            // GB
            T = 1024 * 1024 * 1024L;
            unit = "GB";
        } else if (size < 1024 * 1024 * 1024 * 1024 * 1024L) {
            // TB
            T = 1024 * 1024 * 1024 * 1024L;
            unit = "TB";
        } else {
            //1244037120
        }

        val = size / T + (size * 1.0 % T / T);
        // size%1024=KB
        // size%(1024*1024)=MB
        // size%(1024*1024*1024)=GB
        // size%(1024*1024*1024*1024)=TB
        // size%(1024*1024*1024*1024*1024)=PB
        // size%(1024*1024*1024*1024*1024*1024)=EB
        // size%(1024*1024*1024*1024*1024*1024*1024)=ZB
        // size%(1024*1024*1024*1024*1024*1024*1024*1024)=YB
        // size%(1024*1024*1024*1024*1024*1024*1024*1024*1024)=BB

        return String.format(format, val, unit).replaceAll(".00","");
    }

    /**
     * 格式化显示文件大小:<br>
     * 1KB=1024B<br>
     * 1MB=1024KB<br>
     * 1GB=1024MB<br>
     * 1TB=1024GB<br>
     * 1PB=1024TB<br>
     * 1EB=1024PB<br>
     * 1ZB =1024EB<br>
     * 1YB =1024ZB<br>
     * 1BB=1024YB<br>
     *
     * @param size
     * @return
     */
    public static String sizeFormat(Long size) {
        return sizeFormat(size, 2);
    }

    @NotNull
    public static String getExtension(@Nullable String filename) {
        if (TextUtils.isEmpty(filename)) {
            return "";
        } else {
            int index = indexOfExtension(filename);
            return index == -1 ? "" : filename.substring(index + 1);
        }
    }

    public static String getFileNameWithoutExtension(@Nullable String filename) {
        if (filename == null) {
            return null;
        } else {
            int index = indexOfExtension(filename);
            String back= index == -1 ? "" : filename.substring(0,index);
            LogUtil.e("Felix","getFileNameWithoutExtension: "+back+"  "+filename);
            return back;
        }
    }

    public static int indexOfExtension(String filename) {
        if (filename == null) {
            return -1;
        } else {
            int extensionPos = filename.lastIndexOf(46);
            int lastSeparator = indexOfLastSeparator(filename);
            return lastSeparator > extensionPos ? -1 : extensionPos;
        }
    }

    public static int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        } else {
            int lastUnixPos = filename.lastIndexOf(47);
            int lastWindowsPos = filename.lastIndexOf(92);
            return Math.max(lastUnixPos, lastWindowsPos);
        }
    }

    /**
     * 通过Uri返回File文件
     * 注意：通过相机的是类似content://media/external/images/media/97596
     * 通过相册选择的：file:///storage/sdcard0/DCIM/Camera/IMG_20150423_161955.jpg
     * 通过查询获取实际的地址
     * @param uri
     * @return
     */
    public static File getFileByUri(Uri uri,Context context) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getApplicationContext().getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA }, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getApplicationContext().getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File(path);
        } else {
            Log.i(TAG, "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }
}
