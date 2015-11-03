package com.github.mfursov.kortik

import android.support.v7.internal.view.menu.ActionMenuItemView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.github.mfursov.kortik.util.KortikLogger
import org.jetbrains.anko.debug

class PlayBarController(activity: MainActivity) : AppStateListener, KortikLogger {
    val activity: MainActivity

    init {
        this.activity = activity
    }

    fun onCreate() {
        debug { "PlayBarController::onCreate" }
        Kortik.addStateListener(this)
        val toolbar = activity.findViewById(R.id.play_bar) as Toolbar
        toolbar.inflateMenu(R.menu.playback_menu)
        toolbar.setOnMenuItemClickListener { onOptionsItemSelected(it) }
    }

    fun onDestroy() = Kortik.removeStateListener(this)


    override fun onStateChanged(state: AppState) {
        debug { "PlayBarController::onStateChanged" }
        (activity.findViewById(R.id.action_stop_playback) as ActionMenuItemView).isEnabled = state.playingFile != null
    }

    fun onOptionsItemSelected(item: MenuItem): Boolean {
        debug { "PlayBarController::onOptionsItemSelected $item" }
        when {
            item.itemId == R.id.action_stop_playback -> stopPlayback()
        }
        return true;
    }
}
