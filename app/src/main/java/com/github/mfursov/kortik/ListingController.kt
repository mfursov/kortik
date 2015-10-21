package com.github.mfursov.kortik

import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.github.mfursov.kortik.util.KortikLogger
import com.github.mfursov.kortik.util.getListing
import org.jetbrains.anko.debug
import org.jetbrains.anko.find
import java.io.File
import java.util.ArrayList

class ListingController(val listingView: ListingView) : LoaderManager.LoaderCallbacks<List<File>>, KortikLogger, AppStateListener {

    val listingAdapter: ListingAdapter
    val fileLoader: Loader<List<File>>
    val emptyLabel: TextView
    val listView: ListView

    init {
        listView = listingView.view.find<ListView>(android.R.id.list)
        emptyLabel = listingView.view.find<TextView>(R.id.empty_listing_label)
        emptyLabel.visibility = View.GONE
        emptyLabel.setOnClickListener { folderUp() }
        listingAdapter = ListingAdapter(listingView.context, R.layout.row_listing, ArrayList())
        listingView.listAdapter = listingAdapter
        fileLoader = listingView.loaderManager.initLoader(0, null, this) //todo: add loader ID to resources and reuse
        fileLoader.forceLoad()
        Kortik.addStateListener(this)
        debug { "ListingController::initialized" }
    }

    fun onDestroy() {
        debug { "ListingController::onDestroy" }
        Kortik.removeStateListener(this);
    }

    private fun setListing(listing: List<File>) {
        debug { "ListingController::setListing: $listing" }
        listingAdapter.setNotifyOnChange(false);
        listingAdapter.clear()
        listingAdapter.addAll(listing)
        listingAdapter.notifyDataSetChanged()
        emptyLabel.visibility = if (listing.isEmpty()) View.VISIBLE else View.GONE;
        listView.visibility = if (listing.isEmpty() ) View.GONE else View.VISIBLE;
    }

    fun listItemClicked(position: Int) = openFile(listingAdapter.getItem(position))

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<File>> {
        debug { "ListingController::onCreateLoader" }
        var fileLoader = object : AsyncTaskLoader<List<File>>(listingView.context) {
            override fun loadInBackground(): List<File> {
                debug { "ListingController::loadInBackground" }
                return getListing(Kortik.state.listingDir)
            }
        }
        return fileLoader
    }

    override fun onLoadFinished(loader: Loader<List<File>>?, data: List<File>) = setListing(data)

    //not used for this data source.
    override fun onLoaderReset(loader: Loader<List<File>>) {
        debug { "ListingController::onLoaderReset" }
        listingAdapter.clear()
        listingAdapter.notifyDataSetChanged()
    }

    public fun refresh() {
        debug { "ListingController::refresh" }
        fileLoader.onContentChanged();
    }

    override fun onStateChanged(state: AppState) = refresh()
}
