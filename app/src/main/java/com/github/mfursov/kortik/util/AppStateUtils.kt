package com.github.mfursov.kortik.util

import android.media.MediaPlayer
import com.github.mfursov.kortik.AppState
import java.io.File

fun AppState.withListingDir(listingDir: File): AppState {
    return AppState(listingDir, playingFile, mediaPlayer)
}

fun AppState.withPlayingFile(mediaPlayer: MediaPlayer?, playingFile: File?): AppState {
    return AppState(listingDir, playingFile, mediaPlayer)
}