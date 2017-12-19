package com.github.mfursov.kortik

import android.app.Application

class KortikApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Kortik.appContext = this
    }
}
