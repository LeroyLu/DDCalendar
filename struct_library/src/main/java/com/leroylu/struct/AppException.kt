package com.leroylu.struct

/**
 * @author jiaj.lu
 * @date 2020/8/21
 * @description
 */
class AppException(
    val code: Int = NORMAL_TIP,
    msg: String? = null
) : Throwable(msg) {

    companion object {
        const val NORMAL_TIP = 1
        const val NO_MORE_DATA = 2
    }
}