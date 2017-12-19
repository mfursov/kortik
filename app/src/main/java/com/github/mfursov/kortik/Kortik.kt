package com.github.mfursov.kortik

import android.media.MediaPlayer
import com.github.mfursov.kortik.util.KortikLogger
import com.github.mfursov.kortik.util.getDefaultListingDir
import org.jetbrains.anko.debug
import java.io.File
import java.util.ArrayList
import java.util.Collections

object Kortik : KortikLogger {
    var appContext: KortikApp? = null

    var state = AppState(getDefaultListingDir(), null)
        set(newState) {
            if (newState == field) {
                return
            }
            debug { "setState: $field -> $newState" }
            field = newState
            stateListeners.forEach { it.onStateChanged(field) }
        }

    private val stateListeners: MutableList<AppStateListener> = Collections.synchronizedList(ArrayList())

    fun addStateListener(listener: AppStateListener) {
        stateListeners += listener
    }

    fun removeStateListener(listener: AppStateListener) {
        stateListeners -= listener
    }
}

interface AppStateListener {
    fun onStateChanged(state: AppState)
}

data class AppState(
        val listingDir: File,
        val playingFile: File ?,
        val mediaPlayer: MediaPlayer? = null
) {}
