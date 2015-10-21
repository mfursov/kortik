package com.github.mfursov.kortik

import android.os.Bundle
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.github.mfursov.kortik.util.KortikLogger
import org.jetbrains.anko.debug

/**
 * A placeholder fragment containing a simple view.
 */
class ListingView : ListFragment(), KortikLogger {

    var controller: ListingController? = null

    /**
     * Overriding base method of the ListFragment because we use more complex layout with list.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        debug { "ListingView::onCreateView: bundle: $savedInstanceState" }
        return inflater.inflate(R.layout.fragment_listing, container, false)
    }

    override fun onResume() {
        super.onResume()
        controller = ListingController(this)
    }

    override fun onPause() {
        debug { "ListingView::onPause" }
        super.onPause()
        controller?.onDestroy()
        controller = null;
    }

    override fun onListItemClick(listView: ListView?, view: View?, position: Int, id: Long) {
        debug { "ListingView::onListItemClick: $position" }
        super.onListItemClick(listView, view, position, id)
        controller?.listItemClicked(position)
    }
}
