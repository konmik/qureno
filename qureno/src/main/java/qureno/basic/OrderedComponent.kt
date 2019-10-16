package qureno.basic

import android.content.Context
import android.view.ViewGroup
import qureno.components.*
import qureno.core.Action
import qureno.core.combineReduce
import qureno.util.takeOr

typealias CreateViewGroup = (Context) -> ViewGroup

fun <T> orderedViewGroupComponent(children: Array<out Component<T>>, create: CreateViewGroup): Component<T> {

    val reduces = children.map { it.reduce }
    val updateNodeChildren = children.mapIndexed { index, component -> component.updateNode.focusChild(index) }
    val updateViewChildren = children.map { component -> component.updateViewNode }

    return component(
        reduce = combineReduce(reduces.toTypedArray()),
        updateNode = combineUpdateNode(updateNodeChildren.toTypedArray()),
        updateViewNode = combineUpdateViewNode(updateViewChildren.toTypedArray(), create)
    )
}

private fun <T> combineUpdateViewNode(updates: Array<UpdateViewNode<T>>, create: CreateViewGroup): UpdateViewNode<T> =
    { state, action ->
        var viewIndex = 0
        val groupNode = takeOr(view != null) { copy(view = create(context)) }
        updates.foldIndexed(groupNode) { key, acc, update ->
            acc.updateChildViewNode(state, action, key, update, viewIndex).also {
                if (it.view != null) viewIndex++
            }
        }
    }

inline fun <T> ViewNode.updateChildViewNode(state: T, action: Action, key: Any, updateChild: UpdateViewNode<T>, viewIndex: Int): ViewNode {
    val group = view as ViewGroup? ?: return this
    val old = children[key]
    val new = updateChild(old ?: ViewNode(dispatch, context), state, action)
    return takeOr(old === new) {
        if (old?.view !== new.view) {
            if (old?.view != null) {
                group.removeViewAt(viewIndex)
            }
            if (new.view != null) {
                group.addView(new.view, viewIndex)
                new.onViewAdded?.invoke()
            }
        }
        copy(children = children + (key to new))
    }
}
