package me.spiffylogic.wardrobeshuffle.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import me.spiffylogic.wardrobeshuffle.data.WardrobeContract.WardrobeEntry
import java.util.*

//const val SAMPLE_FILENAME = "sample.jpg"

class WardrobeDbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        const val DB_NAME = "wardrobe.db"
        const val DB_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_ITEMS_TABLE =
                String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT);",
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

    fun insertItem(imagePath: String?, desc: String?) {
        val values = ContentValues()
        values.put(WardrobeEntry.COLUMN_DESC, desc)
        values.put(WardrobeEntry.COLUMN_IMAGE, imagePath)
        val rowId = writableDatabase.insert(WardrobeEntry.TABLE_NAME, null, values)
        assert(rowId != -1L)
    }

    fun updateItem(id: Int, imagePath: String?, desc: String?) {
        val values = ContentValues()
        values.put(WardrobeEntry.COLUMN_DESC, desc)
        values.put(WardrobeEntry.COLUMN_IMAGE, imagePath)
        val rowsAffected = writableDatabase.update(WardrobeEntry.TABLE_NAME, values, WardrobeEntry._ID + "=?", arrayOf(id.toString()))
        assert(rowsAffected == 1)
    }

    fun deleteItem(id: Int) {
        val rowsAffected = writableDatabase.delete(WardrobeEntry.TABLE_NAME,
                WardrobeEntry._ID + "=?",
                arrayOf(id.toString()))
        assert(rowsAffected == 1)
    }

    /*
    fun insertFakeData(context: Context): Long {
        val values = ContentValues()
        values.put(WardrobeEntry.COLUMN_DESC, "test1")

        loadSampleAsset(context)
        val imagePath = context.filesDir.path + "/" + SAMPLE_FILENAME
        values.put(WardrobeEntry.COLUMN_IMAGE, imagePath)

        val rowId = writableDatabase.insert(WardrobeEntry.TABLE_NAME, null, values)
        assert(rowId != -1L)
        return rowId
    }

    private fun loadSampleAsset(context: Context) {
        // load it from assets into internal storage
        val inputStream = context.assets.open(SAMPLE_FILENAME)
        var bytes = ByteArray(inputStream.available())
        inputStream.read(bytes)
        inputStream.close()

        val outputStream: FileOutputStream

        try {
            outputStream = context.openFileOutput(SAMPLE_FILENAME, Context.MODE_PRIVATE)
            outputStream.write(bytes)
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    */

    fun getItem(id: Int): WardrobeItem? {
        var cursor = readableDatabase.query(WardrobeEntry.TABLE_NAME, null, "_id=?", arrayOf(id.toString()), null, null, null)
        if (!cursor.moveToNext()) return null

        val item = WardrobeItem()
        item.id = cursor.getInt(cursor.getColumnIndex(WardrobeEntry._ID))
        item.description = cursor.getString(cursor.getColumnIndex(WardrobeEntry.COLUMN_DESC))
        item.imagePath = cursor.getString(cursor.getColumnIndex(WardrobeEntry.COLUMN_IMAGE)) ?: ""

        cursor.close()
        return item
    }

    fun getAllItems(): ArrayList<WardrobeItem> {
        val items = ArrayList<WardrobeItem>()
        val cursor = readableDatabase.query(WardrobeEntry.TABLE_NAME, null, null, null, null, null, null)

        while (cursor.moveToNext()) {
            val item = WardrobeItem()
            item.id = cursor.getInt(cursor.getColumnIndex(WardrobeEntry._ID))
            item.description = cursor.getString(cursor.getColumnIndex(WardrobeEntry.COLUMN_DESC))
            item.imagePath = cursor.getString(cursor.getColumnIndex(WardrobeEntry.COLUMN_IMAGE)) ?: ""
            items.add(item)
        }
        cursor.close()
        return items
    }

    fun getRandomItem(): WardrobeItem {
        val cursor = readableDatabase.query(WardrobeEntry.TABLE_NAME, null, null, null, null, null, null)
        val n = cursor.count
        val p = Random().nextInt(n) // 0 <= p < n
        assert(cursor.moveToPosition(p)) // zero-based position

        val item = WardrobeItem()
        item.id = cursor.getInt(cursor.getColumnIndex(WardrobeEntry._ID))
        item.description = cursor.getString(cursor.getColumnIndex(WardrobeEntry.COLUMN_DESC))
        item.imagePath = cursor.getString(cursor.getColumnIndex(WardrobeEntry.COLUMN_IMAGE)) ?: ""
        cursor.close()

        Log.d("Markus", String.format("Randomly chose ID %d at position %d", item.id, p))

        return item
    }
}