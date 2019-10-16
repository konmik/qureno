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
    val context: Context,
    val view: View? = null, // gets attached to parent node's view after recreation
    val onViewAdded: (() -> Unit)? = null, // gets called after view gets added to parent ViewGroup
    val children: Map<Any, ViewNode> = mapOf(),
    val boundValue: Any? = null
)
