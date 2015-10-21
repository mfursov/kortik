package com.github.mfursov.kortik.util

import org.jetbrains.anko.AnkoLogger

/**
 * AnkoLogger with tag value set to 'Kortik'
 */
interface KortikLogger : AnkoLogger {
    override val loggerTag: String
        get() = "Kortik"
}
