package com.github.mfursov.kortik.action

import android.content.ActivityNotFoundException
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.PowerManager
import com.github.mfursov.kortik.Constants
import com.github.mfursov.kortik.Kortik
import com.github.mfursov.kortik.PlaybackService
import com.github.mfursov.kortik.util.log
import com.github.mfursov.kortik.util.withPlayingFile
import org.jetbrains.anko.debug
import org.jetbrains.anko.error
import org.jetbrains.anko.longToast
import org.jetbrains.anko.newTask
import java.io.File


fun openFile(file: File) {
    log.debug { "Open file: $file" }
    if (file.isDirectory) {
        changeListingDirTo(file)
        return
    }
    val context = Kortik.appContext ?: return

    //todo: create common place to handle media extensions
    if (!file.extension.toLowerCase().endsWith("mp3")) {
        try {
            context.startActivity(Intent().newTask().setData(Uri.parse(file.absolutePath)))
        } catch (e: ActivityNotFoundException) {
            context.longToast("System doesn't know how to handle that file type!")
        }
        return
    }
    if (Kortik.state.playingFile == file) {
        stopPlayback()
        return
    }
    startPlayback(file)
}


private fun startPlayback(file: File) {
    val context = Kortik.appContext ?: return
    try {
        if (Kortik.state.mediaPlayer?.isPlaying == true) {
            stopPlayback()
        }
        val player = MediaPlayer.create(context, Uri.fromFile(file))
        Kortik.state = Kortik.state.withPlayingFile(player, file)
        player.start()
        player.setWakeMode(Kortik.appContext, PowerManager.PARTIAL_WAKE_LOCK)
        player.setOnCompletionListener({ val nextFile = getNextMediaFile(file); if (nextFile != null) startPlayback(nextFile) })

        // start service with foreground notification
        context.startService(Intent(context, PlaybackService::class.java).setAction(Constants.START_FOREGROUND_ACTION))
    } catch(e: Exception) {
        context.longToast("Failed to start media player for file!")
        log.error("", e)
    }
}


fun playNextFile(prev: Boolean = false) {
    log.debug { "playNextFile: ${Kortik.state.playingFile}, prev: $prev" }
    val file = Kortik.state.playingFile ?: return
    val nextFile = getNextMediaFile(file, prev) ?: return
    startPlayback(nextFile)
}

fun togglePausePlayback() {
    log.debug { "pausePlayback: ${Kortik.state.playingFile}" }
    try {
        val player = Kortik.state.mediaPlayer ?: return
        if (player.isPlaying) {
            player.pause()
        } else {
            player.start()
            player.setWakeMode(Kortik.appContext, PowerManager.PARTIAL_WAKE_LOCK)
        }
    } catch(e: Exception) {
        Kortik.appContext?.longToast("Failed to pause/resume media player!")
        log.error("", e)
    }
}

fun stopPlayback() {
    log.debug { "stopPlayback: ${Kortik.state.playingFile}" }
    try {
        Kortik.state.mediaPlayer?.stop()
        Kortik.state.mediaPlayer?.release()
        Kortik.state = Kortik.state.withPlayingFile(null, null)
        Kortik.appContext?.startService(Intent(Kortik.appContext, PlaybackService::class.java).setAction(Constants.STOP_FOREGROUND_ACTION))
    } catch(e: Exception) {
        Kortik.appContext?.longToast("Failed to stop media player!")
        log.error("", e)
    }
}

fun getNextMediaFile(file: File, prev: Boolean = false): File? {
    val files: Array<out File> = file.parentFile.listFiles() ?: return null
    var mp3Files = files.filter { it.isFile and it.canRead() and (it.extension.toLowerCase() == "mp3") }
    mp3Files = mp3Files.sorted()
    val idx = mp3Files.indexOf(file)
    if (idx < 0) {
        return null
    }
    if (prev) {
        return if (idx == 0) null else mp3Files[idx - 1]
    }
    return if (idx >= mp3Files.size - 1) null else mp3Files[idx + 1]
}
