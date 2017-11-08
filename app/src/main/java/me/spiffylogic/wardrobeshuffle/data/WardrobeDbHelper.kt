package me.spiffylogic.wardrobeshuffle.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import me.spiffylogic.wardrobeshuffle.data.WardrobeContract.WardrobeEntry

class WardrobeDbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME = "wardrobe.db"
        const val DB_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_ITEMS_TABLE =
                String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s BLOB);",
                        WardrobeEntry.TABLE_NAME,
                        WardrobeEntry._ID,
                        WardrobeEntry.COLUMN_DESC,
                        WardrobeEntry.COLUMN_IMAGE)
        db?.execSQL(CREATE_ITEMS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(String.format("DROP TABLE IF EXISTS %s", WardrobeEntry.TABLE_NAME))
        onCreate(db)
    }

    fun insertFakeData(): Long {
        val values = ContentValues()
        values.put(WardrobeEntry.COLUMN_DESC, "test1")
        val rowId = writableDatabase.insert(WardrobeEntry.TABLE_NAME, null, values)
        assert(rowId != -1L)
        return rowId
    }

    fun getAllItems(): Cursor {
        return writableDatabase.query(WardrobeEntry.TABLE_NAME, null, null, null, null, null, null)
    }

}