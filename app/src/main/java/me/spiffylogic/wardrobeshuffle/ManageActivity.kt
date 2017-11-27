package me.spiffylogic.wardrobeshuffle

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.GridLayoutManager
import me.spiffylogic.wardrobeshuffle.data.WardrobeDbHelper

class ManageActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var wardrobeAdapter = WardrobeAdapter()
    private var dbHelper: WardrobeDbHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)

        dbHelper = WardrobeDbHelper(this)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView?.layoutManager = GridLayoutManager(this, 2)
        recyclerView?.adapter = wardrobeAdapter

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            startActivity(Intent(this, EditActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        dbHelper?.let {
            wardrobeAdapter.items = it.getAllItems()
            wardrobeAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper?.close()
    }
}
