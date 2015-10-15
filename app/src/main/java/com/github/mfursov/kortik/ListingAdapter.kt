package com.github.mfursov.kortik

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.io.File
import kotlin.test.assertNotNull

class ListingAdapter : ArrayAdapter<File> {
    private val activityContext: Context;
    private val resource: Int;
    private val files: List<File>;

    constructor(c: Context, res: Int, listing: List<File>) : super(c, res, listing) {
        this.activityContext = c
        this.resource = res
        this.files = listing;
    }

    override fun getItem(i: Int): File {
        return files.get(i)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v: View? = convertView
        if (v == null) {
            val inflater = (LayoutInflater.from(activityContext))
            v = inflater.inflate(resource, null)
            v = assertNotNull(v);
        }


        val nameView = v.findViewById(R.id.name_text_view) as TextView
        val detailsView = v.findViewById(R.id.details_text_view) as TextView
        val file = getItem(position)

        if (!file.isDirectory) {
            if (file.length() > 0) {
                detailsView.text = "" + file.length();
            }
        }
        nameView.text = file.name
        return v
    }
}
