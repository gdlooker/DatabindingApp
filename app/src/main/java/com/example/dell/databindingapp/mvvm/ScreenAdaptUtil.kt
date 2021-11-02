package com.example.dell.databindingapp.mvvm


import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi


/**
 * ui适配工具
 */
object ScreenAdaptUtil {
    private val STANDER_SCREEN_WIDTH_IN_DP = 375
    private var sNonCompatDensity: Float = 0.toFloat()
    private var sNonCompatScaleDensity: Float = 0.toFloat()
    private var window: Window? = null
    private var dark: Boolean = false

    /**
     * ui适配
     *
     * @param activity
     * @param application
     */
    @RequiresApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    fun setCustomDensity(activity: Activity, application: Application, with: Int = STANDER_SCREEN_WIDTH_IN_DP) {
        val appDisplayMetrics = application.resources.displayMetrics

        if (sNonCompatDensity == 0f) {
            sNonCompatDensity = appDisplayMetrics.density
            sNonCompatScaleDensity = appDisplayMetrics.scaledDensity

            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onConfigurationChanged(newConfig: Configuration) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNonCompatScaleDensity = application.resources.displayMetrics.scaledDensity
                    }
                }

                override fun onLowMemory() {

                }
            })
        }

        val targetDensity = appDisplayMetrics.widthPixels.toFloat() / with
        val targetScaleDensity = targetDensity * (sNonCompatScaleDensity / sNonCompatDensity)
        val targetDensityDpi = (160 * targetDensity).toInt()

        appDisplayMetrics.density = targetDensity
        appDisplayMetrics.scaledDensity = targetScaleDensity
        appDisplayMetrics.densityDpi = targetDensityDpi

        val activityDisplayMetrics = activity.resources.displayMetrics
        activityDisplayMetrics.density = targetDensity
        activityDisplayMetrics.scaledDensity = targetScaleDensity
        activityDisplayMetrics.densityDpi = targetDensityDpi
    }


    /**
     * 设置全屏Android 6.0 以上设置 黑色字体
     */
    fun Activity.setFullScreenBlack() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    fun Activity.setStatusTextBlack() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
    }

    fun Activity.setFullScreenColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = color
        }
    }

    /**
     * 设置全屏Android 6.0 以上设置 白色字体
     */
    fun Activity.setFullScreenWhite() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    fun Activity.setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏显示
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}

