package com.isw.pinencrypter

import android.app.Application
import com.pixplicity.easyprefs.library.Prefs

class LibraryApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        Prefs.Builder()
            .setContext(this)
            .setMode(MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }
}