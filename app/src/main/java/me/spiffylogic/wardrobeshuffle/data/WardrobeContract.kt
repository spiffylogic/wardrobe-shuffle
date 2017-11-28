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
    class HistoryEntry {
        companion object {
            val TABLE_NAME = "history"
            val COLUMN_ITEM_KEY = "item_id"
            val COLUMN_DATE = "date"
            val COLUMN_WORN = "worn" // 1 = worn, 0 = skipped
        }
    }
}