package me.spiffylogic.wardrobeshuffle

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import me.spiffylogic.wardrobeshuffle.data.WardrobeItem
import java.io.File

class WardrobeAdapter : RecyclerView.Adapter<WardrobeAdapter.ViewHolder>() {
    var items: List<WardrobeItem>? = null

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val item = items?.get(position)
        if (item != null) holder?.bindItem(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent!!.context).inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = if (items != null) items!!.count() else 0

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var textView: TextView
        private var imageView: ImageView
        private var wardrobeItem: WardrobeItem? = null

        init {
            textView = v.findViewById(R.id.sampleTextView)
            imageView = v.findViewById(R.id.imageView)
            v.setOnClickListener { v ->
                if (wardrobeItem != null) {
                    val editItemIntent = Intent(v.context, EditActivity::class.java)
                    editItemIntent.putExtra(EditActivity.ITEM_KEY, wardrobeItem)
                    v.context.startActivity(editItemIntent)
                }
            }
        }

        fun bindItem(item: WardrobeItem) {
            wardrobeItem = item
            textView.text = item.description
            if (item.imagePath != "")
                Util.setImageFromFile(File(item.imagePath), imageView)
            else
                imageView.setImageDrawable(null) // clear any previous image from recycled view
        }
    }
}