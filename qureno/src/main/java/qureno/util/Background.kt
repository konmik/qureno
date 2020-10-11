package qureno.util

import android.support.annotation.ColorInt
import qureno.components.Component
import qureno.components.onViewAdded

fun <T> Component<T>.background(@ColorInt color: Int): Component<T> =
    Component(reduce, updateViewNode.onViewAdded { setBackgroundColor(color) })
