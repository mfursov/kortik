package com.github.mfursov.kortik

import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.github.mfursov.kortik.action.canGoUp
import com.github.mfursov.kortik.action.folderUp
import com.github.mfursov.kortik.action.gotoPlaying
import com.github.mfursov.kortik.util.KortikLogger
import org.jetbrains.anko.debug

class NavBarController(activity: AppCompatActivity) : AppStateListener, KortikLogger {
    val activity: AppCompatActivity

    init {
        this.activity = activity
    }

    fun onCreate() = Kortik.addStateListener(this)

    fun onDestroy() = Kortik.removeStateListener(this)


    fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater): Boolean {
        debug { "NavBarController::onCreateOptionsMenu $menu" }
        menuInflater.inflate(R.menu.navigation_menu, menu);
        return true;
    }

    fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        debug { "NavBarController::onPrepareOptionsMenu $menu" }
        activity.supportActionBar.setDisplayHomeAsUpEnabled(canGoUp())
        menu ?: return true;
        menu.findItem(R.id.action_goto_playing).setVisible(Kortik.state.playingFile != null);
        return true;
    }

    override fun onStateChanged(state: AppState) {
        activity.invalidateOptionsMenu();
    }

    fun onOptionsItemSelected(item: MenuItem): Boolean {
        debug { "NavBarController::onOptionsItemSelected $item" }
        when {
            item.itemId == R.id.action_goto_playing -> gotoPlaying()
            item.itemId == android.R.id.home -> folderUp();
        }
        return true;
    }
}
