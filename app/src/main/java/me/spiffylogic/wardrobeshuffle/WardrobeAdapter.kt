package me.spiffylogic.wardrobeshuffle

import android.database.Cursor
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import me.spiffylogic.wardrobeshuffle.data.WardrobeContract
import java.io.File

class WardrobeAdapter(var cursor: Cursor) : RecyclerView.Adapter<WardrobeAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (!cursor.moveToPosition(position)) return

        val desc = cursor.getString(cursor.getColumnIndex(WardrobeContract.WardrobeEntry.COLUMN_DESC))
        val imagePath = cursor.getString(cursor.getColumnIndex(WardrobeContract.WardrobeEntry.COLUMN_IMAGE))

        holder?.textView?.text = desc
        if (imagePath != null)
            holder?.imageView?.setImageURI(Uri.fromFile(File(imagePath)))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent!!.context).inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return cursor.count
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var textView = v.findViewById<TextView>(R.id.sampleTextView)
        var imageView = v.findViewById<ImageView>(R.id.imageView)
    }
}