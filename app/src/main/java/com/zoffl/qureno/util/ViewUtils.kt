package com.zoffl.qureno.util

import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View

fun Context.inflate(@LayoutRes layoutId: Int): View =
    LayoutInflater.from(this).inflate(layoutId, null)

fun View.onClick(@IdRes viewId: Int, callback: () -> Unit) {
    findViewById<View>(viewId).setOnClickListener { callback() }
}
