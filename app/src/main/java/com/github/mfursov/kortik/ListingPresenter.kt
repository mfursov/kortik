package com.github.mfursov.kortik

import android.app.LoaderManager
import android.content.ActivityNotFoundException
import android.content.AsyncTaskLoader
import android.content.Intent
import android.content.Loader
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.find
import org.jetbrains.anko.longToast
import java.io.File
import java.util.ArrayList

class ListingPresenter(val listingView: ListingView) : LoaderManager.LoaderCallbacks<List<File>>, AnkoLogger {

    val state = ListingState()
    val listingAdapter: ListingAdapter = ListingAdapter(listingView.activity, R.layout.row_listing, ArrayList())
    val fileLoader: Loader<List<File>>
    val emptyLabel: TextView
    val listView: ListView

    init {
        listingView.listAdapter = listingAdapter;
        fileLoader = listingView.activity.loaderManager.initLoader(0, null, this);//todo: add loader ID
        fileLoader.forceLoad();
        listView = listingView.find<ListView>(android.R.id.list);
        emptyLabel = listingView.find<TextView>(R.id.empty_listing_label)
        emptyLabel.visibility = View.GONE;
        emptyLabel.setOnClickListener { if (state.goUp()) fileLoader.onContentChanged() }
    }

    private fun updateAdapter(data: List<File>) {
        listingAdapter.clear()
        listingAdapter.addAll(data)
        listingAdapter.notifyDataSetChanged()
        emptyLabel.visibility = if (data.isEmpty()) View.VISIBLE else View.GONE;
        listView.visibility = if (data.isEmpty() ) View.GONE else View.VISIBLE;
    }

    fun listItemClicked(position: Int) {
        val fileClicked = listingAdapter.getItem(position)

        if (fileClicked.isDirectory) {
            state.dir = fileClicked
            if (fileLoader.isStarted) {
                fileLoader.onContentChanged()
            }
        } else {
            openFile(Uri.fromFile(fileClicked))
        }
    }

    private fun openFile(fileUri: Uri) {
        val mimeType = state.getMimeType(fileUri)
        if (mimeType != null) {
            //we have determined a mime type and can probably handle the file.
            try {
                val i = Intent(Intent.ACTION_VIEW)
                i.setDataAndType(fileUri, mimeType)
                listingView.activity.startActivity(i)
            } catch (e: ActivityNotFoundException) {
                listingView.activity.longToast("The System understands this file type, but no applications are installed to handle it.");
            }
        } else {
            listingView.activity.longToast("System doesn't know how to handle that file type!");
        }
    }

    fun homePressed() {
        if (state.goUp()) {
            fileLoader.onContentChanged()
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<File>> {
        var fileLoader = object : AsyncTaskLoader<List<File>>(listingView.activity) {
            override fun loadInBackground(): List<File> {
                debug { "loadInBackground()" }
                return state.getAllFiles(state.dir)
            }
        }
        return fileLoader
    }

    override fun onLoadFinished(loader: Loader<List<File>>?, data: List<File>) {
        updateAdapter(data)
    }

    override fun onLoaderReset(loader: Loader<List<File>>) {
        //not used for this data source.
    }
}
