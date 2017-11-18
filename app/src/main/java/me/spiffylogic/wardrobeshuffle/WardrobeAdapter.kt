package me.spiffylogic.wardrobeshuffle

import android.net.Uri
import android.support.v7.widget.RecyclerView
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
        if (item != null && holder != null) {
            holder.textView?.text = item.description
            if (item.imagePath != "" && holder.imageView != null)
                Util.setImageFromFile(File(item.imagePath), holder.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent!!.context).inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = if (items != null) items!!.count() else 0

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var textView = v.findViewById<TextView>(R.id.sampleTextView)
        var imageView = v.findViewById<ImageView>(R.id.imageView)
    }
}