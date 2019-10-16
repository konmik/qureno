package qureno.util

import qureno.components.Component
import qureno.components.Node
import qureno.components.ViewNode
import qureno.core.Action
import qureno.core.Dispatch
import qureno.core.NoAction

/**
 * Are there really decent use cases for NoScope actions?
 */
interface NoScope

data class ScopeAction(val tag: Any, val action: Action) : Action {
    override fun toString(): String = "$tag -> $action"
}

fun <T> Component<T>.scopeActions(tag: Any): Component<T> =
    Component(
        reduce = { action ->
            reduce(action.unwrap(tag))
        },
        updateNode = { state, action ->
            val old = children[Unit]
            val new = (old ?: Node(dispatch.wrap(tag))).updateNode(state, action.unwrap(tag))
            takeOr(old === new) { copy(children = mapOf(Unit to new)) }
        },
        updateViewNode = { state, action ->
            val old = children[Unit]
            val new = (old ?: ViewNode(dispatch.wrap(tag), context)).updateViewNode(state, action.unwrap(tag))
            takeOr(old === new) { copy(view = new.view, children = mapOf(Unit to new)) }
        }
    )

private fun Action.wrap(tag: Any) =
    if (this is NoScope) this else ScopeAction(tag, this)

private fun Action.unwrap(tag: Any): Action =
    if (this is ScopeAction) {
        if (this.tag == tag) action else NoAction
    } else {
        this
    }

private fun Dispatch.wrap(tag: Any): Dispatch =
    { this(it.wrap(tag)) }
