package qureno.components

import android.content.Context
import android.view.View
import qureno.core.Dispatch

/**
 * ViewNode gets recreated every time node or view or any children get changed.
 *
 * When state gets changed it doesn't normally mean that ViewNode will be recreated, but it can be the case.
 */
data class ViewNode(
    val dispatch: Dispatch,
    val context: Context? = null,
    val view: View? = null,
    val onViewAdded: (() -> Unit)? = null,
    val children: Map<Any, ViewNode> = mapOf(),
    val unsubscribe: Map<Any, Unsubscribe> = mapOf(),
    val boundValue: Any? = null
)
