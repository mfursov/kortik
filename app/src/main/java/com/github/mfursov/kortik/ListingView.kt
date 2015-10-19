package com.github.mfursov.kortik

import android.os.Bundle
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.github.mfursov.kortik.util.KortikLogger
import org.jetbrains.anko.debug

/**
 * A placeholder fragment containing a simple view.
 */
class ListingView : ListFragment(), KortikLogger {

    //This is a passive view, so a presenter handles all of the updating, etc.
    var presenter: ListingController? = null;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_listing, container, false)
    }

    //This is a good place to do final initialization as the Fragment is finished initializing itself.
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        debug { "onActivityCreated: bundle: $savedInstanceState" }
        super.onActivityCreated(savedInstanceState);
        presenter = ListingController(this);
        setHasOptionsMenu(true);
    }

    override fun onListItemClick(listView: ListView?, view: View?, position: Int, id: Long) {
        debug { "onListItemClick: $position" }
        super.onListItemClick(listView, view, position, id);
        presenter?.listItemClicked(position);
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var id = item?.itemId;
        when (id) {
            android.R.id.home -> presenter?.homePressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
