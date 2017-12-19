package com.github.mfursov.kortik

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import com.github.mfursov.kortik.action.playNextFile
import com.github.mfursov.kortik.action.stopPlayback
import com.github.mfursov.kortik.action.togglePausePlayback

class RemoteControlReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) {
            return
        }
        val event: KeyEvent? = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT)
        if (event == null || event.action != KeyEvent.ACTION_DOWN) {
            return
        }

        when (event.keyCode) {
            KeyEvent.KEYCODE_MEDIA_STOP -> stopPlayback()
            KeyEvent.KEYCODE_HEADSETHOOK -> togglePausePlayback()
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> togglePausePlayback()
            KeyEvent.KEYCODE_MEDIA_NEXT -> playNextFile(false)
            KeyEvent.KEYCODE_MEDIA_PREVIOUS -> playNextFile(true)
        }
    }

}
