package me.spiffylogic.wardrobeshuffle

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_shuffle.*
import me.spiffylogic.wardrobeshuffle.data.WardrobeDbHelper
import java.io.File

class ShuffleActivity : AppCompatActivity() {
    var dbHelper: WardrobeDbHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shuffle)

        dbHelper = WardrobeDbHelper(this)
        shuffle()
    }

    fun shuffle() {
        // Pick a random item from the database
        val dbHelper = dbHelper
        dbHelper?.let {
            val item = dbHelper.getRandomItem()
            Util.setImageFromFile(File(item.imagePath), image_view)
        }
    }

    fun yesButtonTapped(v: View) {
        finish() // great - all done
    }

    fun noButtonTapped(v: View) {
        shuffle() // try another
    }
}
