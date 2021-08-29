package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.BuildConfig
import timber.log.Timber

class PoliticalPreparedness : Application() {

    companion object {
        lateinit var instance: PoliticalPreparedness
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}