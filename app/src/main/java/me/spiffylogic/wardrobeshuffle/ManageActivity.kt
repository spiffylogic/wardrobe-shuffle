package me.spiffylogic.wardrobeshuffle

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import me.spiffylogic.wardrobeshuffle.data.WardrobeContract.WardrobeEntry
import me.spiffylogic.wardrobeshuffle.data.WardrobeDbHelper

class ManageActivity : AppCompatActivity() {

    var db: SQLiteDatabase? = null
    val items = listOf("white T-shirt", "blue plaid shirt", "festive top", "another one", "striped shirt")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = WardrobeAdapter(items)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val dbHelper = WardrobeDbHelper(this)
        dbHelper.insertFakeData()
        val c = dbHelper.getAllItems()
        assert(c.count > 0)
    }
}
