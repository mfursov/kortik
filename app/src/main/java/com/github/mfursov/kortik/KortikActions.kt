package com.github.mfursov.kortik

import com.github.mfursov.kortik.util.log
import com.github.mfursov.kortik.util.withListingDir
import org.jetbrains.anko.browse
import org.jetbrains.anko.debug
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import java.io.File


fun folderUp() {
    log.debug { "Go up from ${Kortik.state.listingDir}" }
    val parentDir = Kortik.state.listingDir.parentFile;
    if (parentDir?.canRead() ?: false) {
        changeListingDir(parentDir)
    }
}

fun changeListingDir(dir: File) {
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
        changeListingDir(file)
        return;
    }
    val rc = Kortik.appContext?.browse(file.absolutePath) ?: false //todo: stackTrace?
    if (!rc) {
        Kortik.appContext?.longToast("System doesn't know how to handle that file type!");
    }
}