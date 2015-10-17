package com.github.mfursov.kortik.util

import com.github.mfursov.kortik.AppState
import java.io.File

fun AppState.withListingDir(listingDir: File): AppState {
    return AppState(listingDir, playingFile)
}