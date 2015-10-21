package com.github.mfursov.kortik

import android.content.ActivityNotFoundException
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import com.github.mfursov.kortik.util.log
import com.github.mfursov.kortik.util.withListingDir
import com.github.mfursov.kortik.util.withPlayingFile
import org.jetbrains.anko.debug
import org.jetbrains.anko.error
import org.jetbrains.anko.longToast
import org.jetbrains.anko.newTask
import org.jetbrains.anko.toast
import java.io.File


fun canGoUp(): Boolean {
    return Kortik.state.listingDir.parentFile?.canRead() ?: false;
}

fun folderUp() {
    log.debug { "Go up from ${Kortik.state.listingDir}" }
    if (canGoUp()) {
        changeListingDirTo(Kortik.state.listingDir.parentFile)
    }
}

fun changeListingDirTo(dir: File) {
    log.debug { "Change dir to  $dir" }
    if (dir.isDirectory && dir.canRead()) {
        Kortik.state = Kortik.state.withListingDir(dir)
    } else {
        Kortik.appContext?.toast("Can't access to that dir");
    }
}

fun openFile(file: File) {
    log.debug { "Open file: $file" }
    if (file.isDirectory) {
        changeListingDirTo(file)
        return;
    }
    val context = Kortik.appContext ?: return

    //todo: create common place to handle media extensions
    if (!file.extension.toLowerCase().endsWith("mp3")) {
        try {
            context.startActivity(Intent().newTask().setData(Uri.parse(file.absolutePath)));
        } catch (e: ActivityNotFoundException) {
            context.longToast("System doesn't know how to handle that file type!");
        }
        return;
    }
    Kortik.state.mediaPlayer?.stop()
    if (Kortik.state.playingFile == file) {
        stopPlayback();
        return;
    }
    startPlayback(file)
}

private fun startPlayback(file: File) {
    val context = Kortik.appContext ?: return
    try {
        val mediaPlayer = MediaPlayer.create(context, Uri.fromFile(file))
        Kortik.state = Kortik.state.withPlayingFile(mediaPlayer, file)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener({ val nextFile = getNextMediaFile(file); if (nextFile != null) startPlayback(nextFile) })
    } catch(e: Exception) {
        context.longToast("Failed to start media player for file!")
        log.error("", e)
    }
}

fun getNextMediaFile(file: File): File? {
    val files: Array<out File> = file.parentFile.listFiles() ?: return null
    val mp3Files = files.filter { it.isFile and it.canRead() and it.extension.toLowerCase().equals("mp3") }
    var idx = mp3Files.indexOf(file); // todo: store sort order on playback start.
    if (idx < 0 || idx == mp3Files.size() - 1 ) {
        return null
    }
    return mp3Files.get(idx + 1)
}

fun stopPlayback() {
    log.debug { "stopPlayback: ${Kortik.state.playingFile}" }
    try {
        Kortik.state.mediaPlayer?.stop();
        Kortik.state.mediaPlayer?.release();
        Kortik.state = Kortik.state.withPlayingFile(null, null);
    } catch(e: Exception) {
        Kortik.appContext?.longToast("Failed to stop media player!");
        log.error("", e)
    }
}

fun focusToFile(file: File) {
    changeListingDirTo(file.parentFile);
    // todo:
}

fun gotoPlaying() {
    val file = Kortik.state.playingFile ?: return
    focusToFile(file);
}