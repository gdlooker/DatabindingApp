package com.example.dell.databindingapp.util


/**
 * 16进制字符串 与 byte数组 相互转换工具类
 */
object HexUtils {
    private val HEXES = charArrayOf(
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'
    )

    /**
     * byte数组 转换成 16进制小写字符串
     */
    fun bytes2Hex(bytes: ByteArray?): String {
        if (bytes == null || bytes.isEmpty()) {
            return ""
        }
        val hex = StringBuilder()
        for (b in bytes) {
            hex.append(HEXES[b.toInt() shr 4 and 0x0F])
            hex.append(HEXES[b.toInt() and 0x0F])
        }
        return hex.toString()
    }

    /**
     * 16进制字符串 转换为对应的 byte数组
     */
    fun hex2Bytes(hex: String?): ByteArray {
        if (hex == null || hex.isEmpty()) {
            return "".toByteArray()
        }
        return try {
            val hexChars = hex.toCharArray()
            val bytes = ByteArray(hexChars.size / 2) // 如果 hex 中的字符不是偶数个, 则忽略最后一个
            for (i in bytes.indices) {
                bytes[i] = ("" + hexChars[i * 2] + hexChars[i * 2 + 1]).toInt(16).toByte()
            }
            bytes
        } catch (e: Exception) {
            "".toByteArray()
        }
    }
}