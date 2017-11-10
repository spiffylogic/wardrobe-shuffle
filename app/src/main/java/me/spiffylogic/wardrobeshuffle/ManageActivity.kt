package me.spiffylogic.wardrobeshuffle

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import me.spiffylogic.wardrobeshuffle.data.WardrobeDbHelper

class ManageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val dbHelper = WardrobeDbHelper(this)
        dbHelper.insertFakeData(this)
        val c = dbHelper.getAllItems()
        recyclerView.adapter = WardrobeAdapter(c)
    }
}
