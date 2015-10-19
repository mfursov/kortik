package com.github.mfursov.kortik

import android.app.LoaderManager
import android.content.AsyncTaskLoader
import android.content.Loader
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.github.mfursov.kortik.util.KortikLogger
import com.github.mfursov.kortik.util.find
import com.github.mfursov.kortik.util.getListing
import org.jetbrains.anko.debug
import java.io.File
import java.util.ArrayList

class ListingController(val listingView: ListingView) : LoaderManager.LoaderCallbacks<List<File>>, KortikLogger, AppStateListener {

    val listingAdapter: ListingAdapter = ListingAdapter(listingView.activity, R.layout.row_listing, ArrayList())
    val fileLoader: Loader<List<File>>
    val emptyLabel: TextView
    val listView: ListView

    init {
        listingView.listAdapter = listingAdapter;
        fileLoader = listingView.activity.loaderManager.initLoader(0, null, this);//todo: add loader ID to resources and reuse
        fileLoader.forceLoad();
        listView = listingView.find<ListView>(android.R.id.list);
        emptyLabel = listingView.find<TextView>(R.id.empty_listing_label)
        emptyLabel.visibility = View.GONE;
        emptyLabel.setOnClickListener { folderUp() }
        Kortik.addStateListener(this);
        debug { "ListingPresenter initialized" }
    }

    private fun updateListing(listing: List<File>) {
        debug { "setting listing to: $listing" }
        listingAdapter.setNotifyOnChange(false);
        listingAdapter.clear()
        listingAdapter.addAll(listing)
        listingAdapter.notifyDataSetChanged()
        emptyLabel.visibility = if (listing.isEmpty()) View.VISIBLE else View.GONE;
        listView.visibility = if (listing.isEmpty() ) View.GONE else View.VISIBLE;
    }

    fun listItemClicked(position: Int) = openFile(listingAdapter.getItem(position))

    fun homePressed() = folderUp()

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<File>> {
        var fileLoader = object : AsyncTaskLoader<List<File>>(listingView.activity) {
            override fun loadInBackground(): List<File> {
                debug { "loading listing in background" }
                return getListing(Kortik.state.listingDir)
            }
        }
        return fileLoader
    }

    override fun onLoadFinished(loader: Loader<List<File>>?, data: List<File>) = updateListing(data)

    //not used for this data source.
    override fun onLoaderReset(loader: Loader<List<File>>) {
        listingAdapter.clear()
        listingAdapter.notifyDataSetChanged()
    }

    override fun onStateChanged(state: AppState) {
        debug { "ListingView: onStateChanged" }
        fileLoader.onContentChanged()

    }
}
