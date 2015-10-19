package com.github.mfursov.kortik

import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.github.mfursov.kortik.util.KortikLogger
import org.jetbrains.anko.debug

class NavBarController(activity: AppCompatActivity) : AppStateListener, KortikLogger {
    val activity: AppCompatActivity;

    init {
        this.activity = activity;
    }

    fun onCreate() {
        Kortik.addStateListener(this);
        updateAvailableActions();
    }

    fun onDestroy() = Kortik.removeStateListener(this);

    override fun onStateChanged(state: AppState) = updateAvailableActions();

    private fun updateAvailableActions() {
        activity.supportActionBar.setDisplayHomeAsUpEnabled(canGoUp())
    }

    fun onOptionsItemSelected(item: MenuItem): Boolean {
        debug { "onOptionsItemSelected $item" }
        when {
            item.itemId == R.id.action_stop_playback -> stopPlayback();
        }
        return true;
    }
}
