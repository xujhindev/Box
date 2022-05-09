package com.xujhin.box.utils

import java.text.SimpleDateFormat
import java.util.*

class IdGenerator {

    companion object {
        //获取当前时间戳
        //获取6位随机数
        /**
         * @描述 java生成流水号
         * 14位时间戳 + 6位随机数
         * @作者 shaomy
         * @时间:2017-1-12 上午10:10:41
         * @参数:@return
         * @返回值：String
         */
        val id: Long
            get() {
                var id = ""
                //获取当前时间戳
                val sf = SimpleDateFormat("yyyyMMddHHmmss")
                val temp = sf.format(Date())
                id = temp
                return id.toLong()
            }
    }

}