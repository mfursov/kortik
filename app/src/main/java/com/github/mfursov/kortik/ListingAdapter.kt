package com.github.mfursov.kortik

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import org.jetbrains.anko.find
import java.io.File
import kotlin.test.assertNotNull

class ListingAdapter(val activityContext: Context, val resource: Int, val files: List<File>) : ArrayAdapter<File>(activityContext, resource, files) {

    override infix fun getItem(i: Int): File {
        return files.get(i)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v: View? = convertView
        if (v == null) {
            val inflater = (LayoutInflater.from(activityContext))
            v = inflater.inflate(resource, null)
            v = assertNotNull(v);
        }

        val nameView = v.find<TextView>(R.id.name_text_view)
        val detailsView = v.find<TextView>(R.id.details_text_view)
        val file = getItem(position)

        if (!file.isDirectory) {
            if (file.length() > 0) {
                detailsView.text = "" + file.length()
            }
        }
        nameView.text = file.name
        return v
    }
}
