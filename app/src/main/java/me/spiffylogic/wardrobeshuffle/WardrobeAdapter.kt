package me.spiffylogic.wardrobeshuffle

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.w3c.dom.Text

class WardrobeAdapter(var items: List<String>) : RecyclerView.Adapter<WardrobeAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent!!.context).inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var textView = v.findViewById<TextView>(R.id.sampleTextView)

        fun bind(s: String) {
            textView.text = s
        }
    }
}