package me.spiffylogic.wardrobeshuffle

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager

class ManageActivity : AppCompatActivity() {

    val items = listOf("white T-shirt", "blue plaid shirt", "festive top", "another one", "striped shirt")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = WardrobeAdapter(items)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
    }
}
