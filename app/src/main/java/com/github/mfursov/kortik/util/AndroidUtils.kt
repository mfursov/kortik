package com.github.mfursov.kortik.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
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

fun Context.browse(url: String): Boolean {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setData(Uri.parse(url))
        startActivity(intent)
        return true
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
        return false
    }
}
