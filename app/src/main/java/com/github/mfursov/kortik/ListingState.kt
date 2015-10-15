package com.github.mfursov.kortik

import android.net.Uri
import android.os.Environment
import android.util.Log
import android.webkit.MimeTypeMap
import java.io.File
import java.util.*

class ListingState {
    var dir: File

    init {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            dir = Environment.getExternalStorageDirectory()
            Log.i(TAG, "" + dir)
        } else {
            dir = File("")
            Log.i(TAG, "External storage unavailable")
        }
    }

    fun goUp(): Boolean {
        val file = dir.parentFile;
        if (file == null || !file.canRead()) {
            return false;
        }
        dir = file;
        return true;
    }

    fun getAllFiles(f: File): List<File> {
        var allFiles: Array<File>? = f.listFiles()
        if (allFiles == null) {
            allFiles = emptyArray();
        }
        allFiles.sortWith(object : Comparator<File> {
            override fun compare(f1: File, f2: File): Int {
                if (f1.isDirectory == f2.isDirectory) {
                    return f1.compareTo(f2);
                }
                return if (f1.isDirectory) -1 else 1;
            }
        })
        return allFiles.asList();
    }

    fun getMimeType(uri: Uri): String? {
        var mimeType: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(uri.path)
        if (MimeTypeMap.getSingleton().hasExtension(extension)) {
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return mimeType
    }

    companion object {
        val TAG = "Current dir"
    }
}