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
import java.util.ArrayList

class Presenter : LoaderManager.LoaderCallbacks<List<File>> {

    val fragment: MainActivityFragment;
    val model = Model()
    val fileArrayAdapter: FileArrayAdapter
    var data: List<File> = ArrayList()
    var fileLoader: AsyncTaskLoader<List<File>>? = null

    constructor(fragment: MainActivityFragment) {
        this.fragment = fragment;
        fileArrayAdapter = FileArrayAdapter(fragment.activity, R.layout.list_row, data);
        fragment.listAdapter = fileArrayAdapter;
        fragment.activity.loaderManager.initLoader(0, null, this);

        fileLoader!!.forceLoad();
    }

    private fun updateAdapter(data: List<File>) {
        fileArrayAdapter.clear()
        fileArrayAdapter.addAll(data)
        fileArrayAdapter.notifyDataSetChanged()
    }

    fun listItemClicked(position: Int) {
        val fileClicked = fileArrayAdapter.getItem(position)

        if (fileClicked.isDirectory) {
            model.mPreviousDir = model.currentDir
            model.currentDir = fileClicked
            if (fileLoader!!.isStarted) {
                fileLoader!!.onContentChanged()
            }
        } else {
            openFile(Uri.fromFile(fileClicked))
        }
    }

    private fun openFile(fileUri: Uri) {
        val mimeType = model.getMimeType(fileUri)
        if (mimeType != null) {
            //we have determined a mime type and can probably handle the file.
            try {
                val i = Intent(Intent.ACTION_VIEW)
                i.setDataAndType(fileUri, mimeType)
                fragment.activity.startActivity(i)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(fragment.activity, "The System understands this file type," + "but no applications are installed to handle it.",
                        Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(fragment.activity, "System doesn't know how to handle that file type!", Toast.LENGTH_LONG).show()
        }
    }

    fun homePressed() {
        if (model.canGoBack()) {
            model.currentDir = model.mPreviousDir
            fileLoader!!.onContentChanged()
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<File>> {
        var fileLoader = object : AsyncTaskLoader<List<File>>(fragment.activity) {
            override fun loadInBackground(): List<File> {
                Log.i("Loader", "loadInBackground()")
                return model.getAllFiles(model.currentDir)
            }
        }
        this.fileLoader = fileLoader;
        return fileLoader
    }

    override fun onLoadFinished(loader: Loader<List<File>>?, data: List<File>) {
        this.data = data
        updateAdapter(data)
    }

    override fun onLoaderReset(loader: Loader<List<File>>) {
        //not used for this data source.
    }
}
