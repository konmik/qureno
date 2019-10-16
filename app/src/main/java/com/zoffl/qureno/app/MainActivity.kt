package com.zoffl.qureno.app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zoffl.qureno.demo.RootScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()
        startComponentActivity(RootScreen.State())
    }
}
