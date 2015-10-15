package com.github.mfursov.kortik

import android.app.ListFragment
import android.os.Bundle
import android.view.*
import android.widget.ListView

/**
 * A placeholder fragment containing a simple view.
 */
class ListingView : ListFragment() {

    //This is a passive view, so a presenter handles all of the updating, etc.
    var presenter: ListingPresenter? = null;

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_main, container, false)
    }

    //This is a good place to do final initialization as the Fragment is finished initializing itself.
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState);
        presenter = ListingPresenter(this);

        setHasOptionsMenu(true);
    }

    //When we intercept a click, call through to the appropriate method in the presenter.
    override fun onListItemClick(listView: ListView?, view: View?, position: Int, id: Long) {
        super.onListItemClick(listView, view, position, id);
        presenter!!.listItemClicked(position);
    }

    /* Populate options menu and or action bar with menu from res/menu/menu_main.xml*/
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater!!.inflate(R.menu.menu_main, menu);
    }

    //Called when an item in the menu, or the home button (if enabled) is selected.
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        var id = item!!.itemId;

        when (id) {
            android.R.id.home -> presenter!!.homePressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
