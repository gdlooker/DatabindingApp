package com.example.dell.databindingapp.widget.staticlayout;

import android.annotation.SuppressLint;
import android.text.Editable;

import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shuixin.lib_common.mvvmmodule.widget.staticlayout.EmojiSpannableStringBuilder;

final public class ImageEditableFactory extends Editable.Factory {

  private static final Object sInstanceLock = new Object();
  @GuardedBy("sInstanceLock")
  private static volatile Editable.Factory sInstance;
  @Nullable
  private static Class<?> sWatcherClass;

  @SuppressLint({"PrivateApi"})
  private ImageEditableFactory() {
    try {
      String className = "android.text.DynamicLayout$ChangeWatcher";
      sWatcherClass = this.getClass().getClassLoader().loadClass(className);
    } catch (Throwable var2) {

    }

  }

  public static Editable.Factory getInstance() {
    if (sInstance == null) {
      Object var0 = sInstanceLock;
      synchronized (sInstanceLock) {
        if (sInstance == null) {
          sInstance = new ImageEditableFactory();
        }
      }
    }

    return sInstance;
  }

  public Editable newEditable(@NonNull CharSequence source) {
    return (Editable) (sWatcherClass != null ? EmojiSpannableStringBuilder.Companion.create(sWatcherClass, source)
        : super.newEditable(source));
  }
}