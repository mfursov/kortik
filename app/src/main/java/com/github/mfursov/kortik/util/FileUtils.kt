package com.github.mfursov.kortik.util

import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import org.jetbrains.anko.info
import java.io.File
import java.util.Comparator

class FileUtils : KortikLogger

val log: FileUtils = FileUtils();

fun getDefaultListingDir(): File {
    if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
        log.info { "External storage unavailable" }
        return File("");
    }
    return Environment.getExternalStorageDirectory()
}

fun getListing(f: File): List<File> {
    var allFiles: Array<File>? = f.listFiles()
    if (allFiles == null) {
        allFiles = emptyArray();
    }
    allFiles.sortWith(object : Comparator<File> {
        override fun compare(f1: File, f2: File): Int {
            return if (f1.isDirectory == f2.isDirectory) {
                f1.compareTo(f2);
            } else if (f1.isDirectory) -1 else 1
        }
    })
    return allFiles.asList();
}

fun getMimeType(uri: Uri): String? {
    val extension = MimeTypeMap.getFileExtensionFromUrl(uri.path)
    val typeMap = MimeTypeMap.getSingleton()
    if (typeMap.hasExtension(extension)) {
        return typeMap.getMimeTypeFromExtension(extension)
    }
    return null;
}
