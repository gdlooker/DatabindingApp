package com.example.dell.databindingapp.widget.keyboardutil;

import android.text.InputFilter
import android.text.Spanned


/**
 * @author: Linpeng
 * @date: 2020/11/27
 * @Update:2020/11/27
 * @Description: 金额过滤
 */
class AmountFilter constructor(var digitsBeforeZero: Int, var digitsAfterZero: Int) : InputFilter {

    /**
    CharSequence source,  //输入的文字
    int start,  //开始位置
    int end,  //结束位置
    Spanned dest, //当前显示的内容
    int dstart,  //当前开始位置
    int dend //当前结束位置
     */
    override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
        //直接输入"."返回"0."
        val destStr = dest?.toString() ?: "";
        //".x"删除"x"输出为"."，inputFilter无法处理成"0."，所以只处理直接输入"."的case
        if (".".equals(source)) {
            if(digitsAfterZero==0){
                return ""//如果不带小数点，则无法输入“.”
            }
            if (destStr.isEmpty()) {
                return "0."
            } else if (destStr.contains(".")) {
                return ""
            }else if (!destStr.contains(".")&&dstart==0) {
                return "0."
            } else {
                return null
            }
        }else if("".equals(source)&&destStr.startsWith("0")&&start==0&&end==0){//删除
            //0|.44  这个0不可删除
            return ""
        }else if (dstart == 1 && destStr == "0") {
            return ".${source}"
        } else if (dstart == 0 && destStr.isNotEmpty() && source == "0") {
            return "0."
        }else if (destStr.length > (digitsBeforeZero + 1 + digitsAfterZero)) {
            return "";
        }else if (destStr.startsWith("0")&&dstart == 1) {
            return "";
        }
        val split = destStr.split(".")

        if (split != null && split.isNotEmpty()) {
            val strBeforeZero = split.get(0)?.length ?: 0
            if (strBeforeZero > digitsBeforeZero - 1
                    && dstart <= strBeforeZero
            ) {
                return "";
            }
            if (split.size > 1) {
                val strAfterZero = split.get(1)?.length ?: 0
                if (strAfterZero > digitsAfterZero - 1 && dstart > strBeforeZero) {
                    return "";
                }
            }
        }
        return null
    }
}
