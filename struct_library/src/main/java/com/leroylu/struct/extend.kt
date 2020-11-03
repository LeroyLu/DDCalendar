package com.leroylu.struct

import java.security.MessageDigest

/**
 * @author jiaj.lu
 * @date 2020/9/9
 * @description
 */

fun String.md5(): String {
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return bytes.hex()
}

fun ByteArray.hex(): String {
    return joinToString("") { "%02X".format(it) }
}
