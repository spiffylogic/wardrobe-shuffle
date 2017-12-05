package me.spiffylogic.wardrobeshuffle

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_shuffle.*
import me.spiffylogic.wardrobeshuffle.data.WardrobeDbHelper
import me.spiffylogic.wardrobeshuffle.data.WardrobeItem
import java.io.File

class ShuffleActivity : AppCompatActivity() {
    private val dbHelper: WardrobeDbHelper by lazy { WardrobeDbHelper(this) }
    private var item: WardrobeItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shuffle)

        shuffle()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }

    fun shuffle() {
        // Pick a random item from the database
        val i = dbHelper.getRandomItem()
        if (i.imagePath == "")
            image_view.setImageDrawable(null)
        else
            Util.setImageFromFile(File(i.imagePath), image_view)
        item = i
    }

    fun yesButtonTapped(v: View) {
        // we're wearing this today - update the database accordingly
        item?.let { dbHelper.recordHistory(it.id, true) }
        finish()
    }

    fun noButtonTapped(v: View) {
        // skip this item and try another
        item?.let { dbHelper.recordHistory(it.id, false) }
        shuffle()
    }
}
