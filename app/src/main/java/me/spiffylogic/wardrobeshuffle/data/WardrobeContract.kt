package me.spiffylogic.wardrobeshuffle.data

import android.provider.BaseColumns

class WardrobeContract {
    class WardrobeEntry {
        companion object {
            val TABLE_NAME = "items"
            val _ID = BaseColumns._ID
            val COLUMN_DESC = "desc"
            val COLUMN_IMAGE = "image"
        }
    }
    class WornEntry {
        companion object {
            val TABLE_NAME = "worn"
            val _ID = BaseColumns._ID
            val COLUMN_ITEM_KEY = "items_id"
            val COLUMN_DATE = "date"
        }
    }
}