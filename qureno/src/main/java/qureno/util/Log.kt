package qureno.util

import android.util.Log

internal inline fun d(message: () -> Any) {
    Log.d("LOG", message().toString())
}
