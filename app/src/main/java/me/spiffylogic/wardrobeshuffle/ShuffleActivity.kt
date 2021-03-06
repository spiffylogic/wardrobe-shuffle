package me.spiffylogic.wardrobeshuffle

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_shuffle.*
import me.spiffylogic.wardrobeshuffle.data.WardrobeDbHelper
import me.spiffylogic.wardrobeshuffle.data.WardrobeItem
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

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
        val item = dbHelper.getShuffleItem()
        if (item != null) {
            Log.d("Markus", "Shuffle picked item " + item.id)
            val date = dbHelper.getLastWornDate(item.id)
            bindItem(item, date)
        } else {
            desc_text.text = ""
            last_worn_text.visibility = View.GONE
            image_view.setImageDrawable(null)
            Toast.makeText(this, "Got nothing. Go nude!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun bindItem(item: WardrobeItem, date: Date?) {
        desc_text.text = item.description

        if (date == null)
            last_worn_text.visibility = View.GONE
        else
            last_worn_text.text = "Last worn " + SimpleDateFormat("EEEE, MMM d").format(date)

        if (item.imagePath == "")
            image_view.setImageDrawable(null)
        else
            Util.setImageFromFile(File(item.imagePath), image_view)

        this.item = item
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
