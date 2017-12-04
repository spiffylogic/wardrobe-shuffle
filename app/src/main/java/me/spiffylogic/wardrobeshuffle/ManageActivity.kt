package me.spiffylogic.wardrobeshuffle

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_manage.*
import me.spiffylogic.wardrobeshuffle.data.WardrobeDbHelper

class ManageActivity : AppCompatActivity() {
    private var wardrobeAdapter = WardrobeAdapter()
    private var dbHelper: WardrobeDbHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)

        dbHelper = WardrobeDbHelper(this)

        recycler_view?.layoutManager = GridLayoutManager(this, 2)
        recycler_view?.adapter = wardrobeAdapter

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
