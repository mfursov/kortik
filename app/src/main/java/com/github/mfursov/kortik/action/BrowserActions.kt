package com.github.mfursov.kortik.action

import com.github.mfursov.kortik.Kortik
import com.github.mfursov.kortik.util.log
import com.github.mfursov.kortik.util.withListingDir
import org.jetbrains.anko.debug
import org.jetbrains.anko.toast
import java.io.File

fun canGoUp(): Boolean {
    return Kortik.state.listingDir.parentFile?.canRead() ?: false
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
        Kortik.appContext?.toast("Can't access to that dir")
    }
}


fun focusToFile(file: File) {
    changeListingDirTo(file.parentFile)
    // todo:
}

fun gotoPlaying() {
    val file = Kortik.state.playingFile ?: return
    focusToFile(file)
}
