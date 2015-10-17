package com.github.mfursov.kortik.util

import android.support.v4.app.Fragment
import android.view.View
import org.jetbrains.anko.AnkoLogger

/**
 * AnkoLogger with tag value set to Kortik
 */
interface KortikLogger : AnkoLogger {
    override val loggerTag: String
        get() = "Kortik"
}

inline fun <reified T : View> Fragment.find(id: Int): T = view?.findViewById(id) as T
