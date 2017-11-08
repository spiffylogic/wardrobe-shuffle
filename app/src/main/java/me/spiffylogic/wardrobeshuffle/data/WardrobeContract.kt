package me.spiffylogic.wardrobeshuffle.data

import android.provider.BaseColumns

class WardrobeContract {
    companion object WardrobeEntry {
        val _ID = BaseColumns._ID
        val TABLE_NAME = "items"
        val COLUMN_DESC = "desc"
        val COLUMN_IMAGE = "image"
    }
}