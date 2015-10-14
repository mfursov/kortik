package com.github.mfursov.kortik

import android.net.Uri
import android.os.Environment
import android.util.Log
import android.webkit.MimeTypeMap

import java.io.File
import java.util.ArrayList
import java.util.Collections
import java.util.Stack

class Model {
    var currentDir: File
    private val backStack: Stack<File> = Stack()

    init {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            currentDir = Environment.getExternalStorageDirectory()
            Log.i(TAG, "" + currentDir)
        } else {
            currentDir = File("")
            Log.i(TAG, "External storage unavailable")
        }
    }

    fun canGoBack(): Boolean {
        return !backStack.isEmpty()
    }

    var mPreviousDir: File
        get() = backStack.pop()
        set(mPreviousDir: File) {
            backStack.add(mPreviousDir)

        }

    fun getAllFiles(f: File): List<File> {
        var allFiles: Array<File>? = f.listFiles()
        if (allFiles == null) {
            allFiles = emptyArray();
        }

        /* I want all directories to appear before files do, so I have separate lists for both that are merged into one later.*/
        val dirs = ArrayList<File>()
        val files = ArrayList<File>()

        for (file in allFiles) {
            if (file.isDirectory) {
                dirs.add(file)
            } else {
                files.add(file)
            }
        }

        Collections.sort(dirs)
        Collections.sort(files)
        dirs.addAll(files)

        return dirs
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