package com.github.mfursov.kortik

import com.github.mfursov.kortik.util.KortikLogger
import com.github.mfursov.kortik.util.getDefaultListingDir
import org.jetbrains.anko.debug
import java.io.File
import java.util.ArrayList
import java.util.Collections

object Kortik : KortikLogger {
    var appContext: KortikApp? = null;

    public var state = AppState(getDefaultListingDir(), null)
        set(newState) {
            if (newState == field) {
                return;
            }
            debug { "setState: $field -> $newState" }
            field = newState;
            stateListeners.forEach { it.onStateChanged(field) }
        }

    private val stateListeners: MutableList<AppStateListener> = Collections.synchronizedList(ArrayList())

    public fun addStateListener(listener: AppStateListener) {
        stateListeners += listener;
    }

    public fun removeStateListener(listener: AppStateListener) {
        stateListeners -= listener;
    }
}

interface AppStateListener {
    fun onStateChanged(state: AppState)
}

data class AppState(val listingDir: File, val playingFile: File ?)

fun unused() {
}
