package me.spiffylogic.wardrobeshuffle

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_shuffle.*
import me.spiffylogic.wardrobeshuffle.data.WardrobeDbHelper
import me.spiffylogic.wardrobeshuffle.data.WardrobeItem
import java.io.File

class ShuffleActivity : AppCompatActivity() {
    var dbHelper: WardrobeDbHelper? = null
    var item: WardrobeItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shuffle)

        dbHelper = WardrobeDbHelper(this)
        shuffle()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper?.close()
    }

    fun shuffle() {
        // Pick a random item from the database
        val dbHelper = dbHelper
        dbHelper?.let {
            val i = it.getRandomItem()
            if (i.imagePath == "")
                image_view.setImageDrawable(null)
            else
                Util.setImageFromFile(File(i.imagePath), image_view)
            item = i
        }
    }

    fun yesButtonTapped(v: View) {
        // we're wearing this today - update the database accordingly
        val dbHelper = dbHelper
        val i = item
        if (dbHelper != null && i != null) dbHelper.recordHistory(i.id)
        finish()
    }

    fun noButtonTapped(v: View) {
        shuffle() // try another
    }
}
