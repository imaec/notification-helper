package com.imaec.notificationhelper.model

data class GroupDetailData(
    val time: Long = 0,
    val groupName: String = "",
    val img: ByteArray? = null,
    val content: String = "",
    val size: Int = 0,
    val packageName: String = ""
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GroupDetailData

        if (img != null) {
            if (other.img == null) return false
            if (!img.contentEquals(other.img)) return false
        } else if (other.img != null) return false

        return true
    }

    override fun hashCode(): Int {
        return img?.contentHashCode() ?: 0
    }
}