package com.github.mfursov.kortik

import android.app.LoaderManager
import android.content.ActivityNotFoundException
import android.content.AsyncTaskLoader
import android.content.Intent
import android.content.Loader
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import java.io.File
import java.util.*

class ListingPresenter : LoaderManager.LoaderCallbacks<List<File>> {

    val view: ListingView;
    val state = ListingState()
    val listingAdapter: ListingAdapter
    var fileLoader: Loader<List<File>>

    constructor(view: ListingView) {
        this.view = view;
        listingAdapter = ListingAdapter(view.activity, R.layout.list_row, ArrayList());
        view.listAdapter = listingAdapter;
        fileLoader = view.activity.loaderManager.initLoader(0, null, this);
        fileLoader.forceLoad();
    }

    private fun updateAdapter(data: List<File>) {
        listingAdapter.clear()
        listingAdapter.addAll(data)
        listingAdapter.notifyDataSetChanged()
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
                view.activity.startActivity(i)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(view.activity, "The System understands this file type," + "but no applications are installed to handle it.",
                        Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(view.activity, "System doesn't know how to handle that file type!", Toast.LENGTH_LONG).show()
        }
    }

    fun homePressed() {
        val upDir = state.goUp();
        if (upDir) {
            fileLoader.onContentChanged()
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<File>> {
        var fileLoader = object : AsyncTaskLoader<List<File>>(view.activity) {
            override fun loadInBackground(): List<File> {
                Log.i("Loader", "loadInBackground()")
                return state.getAllFiles(state.dir)
            }
        }
        this.fileLoader = fileLoader;
        return fileLoader
    }

    override fun onLoadFinished(loader: Loader<List<File>>?, data: List<File>) {
        updateAdapter(data)
    }

    override fun onLoaderReset(loader: Loader<List<File>>) {
        //not used for this data source.
    }
}
