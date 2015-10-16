package com.github.mfursov.kortik

import android.app.ListFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

/**
 * A placeholder fragment containing a simple view.
 */
class ListingView : ListFragment() {

    //This is a passive view, so a presenter handles all of the updating, etc.
    var presenter: ListingPresenter? = null;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_listing, container, false)
    }

    //This is a good place to do final initialization as the Fragment is finished initializing itself.
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState);
        presenter = ListingPresenter(this);

        setHasOptionsMenu(true);
    }

    override fun onListItemClick(listView: ListView?, view: View?, position: Int, id: Long) {
        super.onListItemClick(listView, view, position, id);
        presenter?.listItemClicked(position);
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var id = item?.itemId;
        when (id) {
            android.R.id.home -> presenter!!.homePressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
