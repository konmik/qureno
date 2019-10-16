package com.zoffl.qureno.app

import android.app.Application

val app: App
    get() = instance

private lateinit var instance: App

class App : Application() {
    init {
        instance = this
    }
}
