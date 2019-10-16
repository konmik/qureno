package qureno.util

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import qureno.components.*

fun <T> Component<T>.matchParent(): Component<T> =
    Component(reduce, updateNode, updateViewNode.withLayoutParams(matchParent))

fun <T> Component<T>.matchParentHeight(): Component<T> =
    Component(reduce, updateNode, updateViewNode.withLayoutParams(matchParentHeight))

private val matchParent: ApplyLayoutParams = {
    width = MATCH_PARENT
    height = MATCH_PARENT
}

private val matchParentHeight: ApplyLayoutParams = {
    height = MATCH_PARENT
}

private fun <T> UpdateViewNode<T>.withLayoutParams(applyLayoutParams: ApplyLayoutParams): UpdateViewNode<T> =
    { state, action ->
        this@withLayoutParams(state, action)
            .takeOr({ it.applyLayoutParams != null }) {
                copy(applyLayoutParams = applyLayoutParams)
            }
    }
