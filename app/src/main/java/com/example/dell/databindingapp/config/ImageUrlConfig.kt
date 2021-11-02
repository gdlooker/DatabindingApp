package com.example.dell.databindingapp.config


object ImageUrlConfig {
    //图片url拼接前缀
    const val imageUrl = "?x-oss-process=style/"

    //头像样式 宽高相同
    const val header450 = imageUrl + "header450w"
    private const val header250 = imageUrl + "header250w"
    private const val header123 = imageUrl + "header123w"

    //动态样式
    //150*高度自适应
    private const val pulish150w = imageUrl + "pulish150w"

    //150*宽度自适应
    const val pulish150h = imageUrl + "pulish150h"

    //375*高度自适应
    private const val pulish375 = imageUrl + "pulish375"

    //450*宽度自适应
    const val pulish450 = imageUrl + "pulish450"

    //540*高度同比例缩放
    private const val pulish540 = imageUrl + "pulish540"

    //关注样式
    //1500*高自适应
    private const val keep1500 = imageUrl + "keep1500"

    //聊天样式
    //390*480
    private const val chat390 = imageUrl + "chat390"

    //480*390
    private const val chat480 = imageUrl + "chat480"

    //视频封面
    const val videoImage = "?x-oss-process=video/snapshot,t_0,f_jpg,ar_auto,m_fast"

    const val LEVEL_NORMAL = 0
    const val LEVEL_CHAT_390_W = 1
    const val LEVEL_CHAT_480_W = 2
    const val LEVEL_ADAPTER_PULISH150_H = 3
    const val LEVEL_ADAPTER_PULISH150_W = 4
    const val LEVEL_ADAPTER_HEIGHT_PULISH375_H = 5
    const val LEVEL_ADAPTER_WITH_PULISH450_W = 6
    const val LEVEL_ADAPTER_HEIGHT_PULISH540_H = 7
    const val LEVEL_ATTENTION_KEEP1500_H = 8
    const val LEVEL_HEADER_250_W = 9
    const val LEVEL_HEADER_123_W = 10
    const val LEVEL_GIF_TO_IMAGE = 11

    /**
     * 获取对应图片缩放地址值
     */
    fun String?.getUrlByScaleType(scaleType: Int = 0): String {
        return when (scaleType) {
            LEVEL_NORMAL -> {
                this ?: ""
            }
            LEVEL_CHAT_390_W -> {
                this.toChat390()
            }
            LEVEL_CHAT_480_W -> {
                this.toChat480()
            }
            LEVEL_ADAPTER_PULISH150_H -> {
                this.toPulish150h()
            }
            LEVEL_ADAPTER_PULISH150_W -> {
                this.toPulish150w()
            }
            LEVEL_ADAPTER_HEIGHT_PULISH375_H -> {
                this.toPulish375()
            }
            LEVEL_ADAPTER_HEIGHT_PULISH540_H -> {
                this.toPulish540()
            }
            LEVEL_ATTENTION_KEEP1500_H -> {
                this.toKeep1500()
            }
            LEVEL_HEADER_250_W -> {
                this.toHeader250()
            }
            LEVEL_HEADER_123_W -> {
                this.toHeader123()
            }
            LEVEL_GIF_TO_IMAGE -> {
                this.toPulish150w()
//                this.toSetGif2Image()
            }
            else -> {
                this ?: ""
            }
        }
    }

    fun String?.toHeader123(): String {
        return when {
            this == null -> ""
            else -> if (this.contains(".gif")) {
                this.getImageZoom(0.3f)
            } else {
                this + header123
            }
        }
    }

    fun String?.toSetGif2Image(): String {
        return when {
            this == null -> ""
            else ->
                this + pulish150w
        }
    }

    fun String?.toHeader250(): String {
        return when {
            this == null -> ""
            else -> if (this.contains(".gif")) {
                this.getImageZoom(0.4f)
            } else {
                this + header250
            }
        }
    }

    fun String?.toChat390(): String {
        return when {
            this == null -> ""
            else -> if (this.contains(".gif")) {
                this.getImageZoom(0.45f)
            } else {
                this + chat390
            }
        }
    }

    fun String?.toChat480(): String {
        return when {
            this == null -> ""
            else -> if (this.contains(".gif")) {
                this.getImageZoom(0.45f)
            } else {
                this + chat480
            }
        }
    }

    fun String?.toPulish150w(): String {
        return when {
            this == null -> ""
            else -> if (this.contains(".gif")) {
                this.getImageZoom(0.3f)
            } else {
                this + pulish150w
            }
        }
    }

    fun String?.toPulish150h(): String {
        return when {
            this == null -> ""
            else -> if (this.contains(".gif")) {
                this.getImageZoom(0.3f)
            } else {
                this + pulish150h
            }
        }
    }


    fun String?.toKeep1500(): String {
        return when {
            this == null -> {
                ""
            }
            this.contains(".gif") -> {
                this.getImageZoom(0.8f)
            }
            else -> {
                this + keep1500
            }
        }
    }

    fun String?.toPulish375(): String {
        return when {
            this == null -> {
                ""
            }
            this.contains(".gif") -> {
                this.getImageZoom(0.5f)
            }
            else -> {
                this + pulish375
            }
        }
    }

    fun String?.toPulish540(): String {
        return when {
            this == null -> {
                ""
            }
            this.contains(".gif") -> {
                this.getImageZoom(0.7f)
            }
            else -> {
                this + pulish540
            }
        }
    }

    /**
     * 图片缩放
     * ratio压缩百分比0-1
     */
    private fun String.getImageZoom(ratio: Float): String {
        val (w, h) = this.getRemoteUrlWH()
        return when {
            w == 1 || h == 1 -> {
                this
            }
            else -> {
                "$this?x-oss-process=image/resize,m_fixed,h_${(h * ratio).toInt()},w_${(w * ratio).toInt()}"
            }
        }
    }

    /**
     * Url固定格式获取宽高
     */
    private fun String.getRemoteUrlWH(): Pair<Int, Int> {
        return try {
            val list = this.split("_")
            val w = list[list.size - 2].replace("w", "").toInt()
            val h = list[list.size - 1].split(".")[0].replace("h", "").toInt()

            if (w == 0 || h == 0) {
                Pair(1, 1)
            } else {
                Pair(w, h)
            }
        } catch (e: Exception) {
            Pair(1, 1)
        }
    }

}


