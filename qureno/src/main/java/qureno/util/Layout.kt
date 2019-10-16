package qureno.util

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import qureno.components.Component
import qureno.components.onViewAdded

fun <T> Component<T>.matchParent(): Component<T> =
    Component(reduce, updateNode, updateViewNode.onViewAdded {
        layoutParams.width = MATCH_PARENT
        layoutParams.height = MATCH_PARENT
    })

fun <T> Component<T>.matchParentHeight(): Component<T> =
    Component(reduce, updateNode, updateViewNode.onViewAdded {
        layoutParams.height = MATCH_PARENT
    })
