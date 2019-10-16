package qureno.util

import android.support.annotation.ColorInt
import qureno.components.Component
import qureno.components.UpdateViewNode

fun <T> Component<T>.background(@ColorInt color: Int): Component<T> =
    Component(reduce, updateNode, updateViewNode.background(color))

private fun <T> UpdateViewNode<T>.background(@ColorInt color: Int): UpdateViewNode<T> =
    { state, action ->
        val old = view
        this@background(state, action).also {
            if (it.view !== old) {
                it.view?.setBackgroundColor(color)
            }
        }
    }
