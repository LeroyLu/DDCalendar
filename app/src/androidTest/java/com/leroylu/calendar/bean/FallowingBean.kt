package com.leroylu.calendar.bean

data class FallowingBean(
    val code: Int,
    val data: Data,
    val message: String,
    val ttl: Int
)

data class Data(
    val list: List<Item>,
    val re_version: Int,
    val total: Int
)

data class Item(
    val attribute: Int,
    val face: String,
    val mid: Int,
    val mtime: Int,
    val official_verify: OfficialVerify,
    val sign: String,
    val special: Int,
    val tag: List<Int>?,
    val uname: String,
    val vip: Vip
)

data class OfficialVerify(
    val desc: String,
    val type: Int
)

data class Vip(
    val accessStatus: Int,
    val dueRemark: String,
    val label: Label,
    val themeType: Int,
    val vipDueDate: Long,
    val vipStatus: Int,
    val vipStatusWarn: String,
    val vipType: Int
)

data class Label(
    val path: String
)