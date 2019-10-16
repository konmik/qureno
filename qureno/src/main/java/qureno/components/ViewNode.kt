package qureno.components

import android.content.Context
import android.view.View
import android.view.ViewGroup
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
    val applyLayoutParams: ApplyLayoutParams? = null, // gets called after view attachment
    val children: Map<Any, ViewNode> = mapOf(),
    val boundValue: Any? = null
)

typealias ApplyLayoutParams = ViewGroup.LayoutParams.() -> Unit
