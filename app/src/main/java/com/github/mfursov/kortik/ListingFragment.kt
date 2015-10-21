package com.github.mfursov.kortik

import android.os.Bundle
import android.support.v4.app.ListFragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import com.github.mfursov.kortik.util.KortikLogger
import com.github.mfursov.kortik.util.getListing
import org.jetbrains.anko.debug
import org.jetbrains.anko.find
import java.io.File
import java.util.ArrayList

/**
 * A placeholder fragment containing a simple view.
 */
class ListingFragment : ListFragment(), LoaderManager.LoaderCallbacks<List<File>>, KortikLogger, AppStateListener {
    val loaderId = 0 //todo: add loader ID to resources and reuse
    var emptyLabel: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        debug { "ListingFragment::onCreate" }
        super.onCreate(savedInstanceState)
        loaderManager.initLoader(loaderId, null, this)
        listAdapter = ListingAdapter(context, R.layout.row_listing, ArrayList())
    }

    override fun onDestroy() {
        debug { "ListingFragment::onDestroy" }
        loaderManager.destroyLoader(loaderId);
        super.onDestroy()
    }

    /**
     * Overriding base method of the ListFragment because we use more complex layout with a list and empty folder view.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        debug { "ListingFragment::onCreateView: bundle: $savedInstanceState" }
        val view = inflater.inflate(R.layout.fragment_listing, container, false)
        emptyLabel = view.find<android.widget.TextView>(R.id.empty_listing_label)
        return view
    }

    override fun onResume() {
        debug { "ListingFragment::onResume" }
        super.onResume()
        loaderManager.getLoader<Any>(loaderId).forceLoad()
        Kortik.addStateListener(this)
    }

    override fun onPause() {
        debug { "ListingFragment::onPause" }
        Kortik.removeStateListener(this)
        super.onPause()
    }

    override fun onListItemClick(listView: ListView?, view: View?, position: Int, id: Long) {
        debug { "ListingFragment::onListItemClick: $position" }
        openFile(listAdapter as  ListingAdapter getItem(position))
    }

    private fun setListing(listing: List<File>) {
        debug { "ListingFragment::setListing: $listing" }
        val adapter = listAdapter as ListingAdapter;
        adapter.setNotifyOnChange(false);
        adapter.clear()
        adapter.addAll(listing)
        adapter.notifyDataSetChanged()
        emptyLabel!!.visibility = if (listing.isEmpty()) View.VISIBLE else View.GONE;
        listView.visibility = if (listing.isEmpty() ) View.GONE else View.VISIBLE;
    }

    override fun onLoaderReset(loader: Loader<List<File>>?) {
        debug { "ListingFragment::onLoaderReset" }
        val adapter = listAdapter as ListingAdapter;
        adapter.clear()
        adapter.notifyDataSetChanged()
    }

    override fun onLoadFinished(loader: Loader<List<File>>?, data: List<File>) = setListing(data)

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<File>>? {
        debug { "ListingFragment::onCreateLoader" }
        var fileLoader = object : AsyncTaskLoader<List<File>>(context) {
            override fun loadInBackground(): List<File> {
                debug { "ListingFragment::loadInBackground" }
                return getListing(Kortik.state.listingDir)
            }
        }
        return fileLoader
    }

    public fun refresh() {
        debug { "ListingFragment::refresh" }
        loaderManager.getLoader<Any>(loaderId).onContentChanged();
    }

    override fun onStateChanged(state: AppState) = refresh()
}
