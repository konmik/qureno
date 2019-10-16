package qureno.components

import qureno.core.Dispatch
import qureno.util.takeOr

data class Node(
    val dispatch: Dispatch,
    val unsubscribe: Unsubscribe? = null,
    val children: Map<Any, Node> = mapOf()
)

typealias Subscribe<T> = (state: T, dispatch: Dispatch) -> Unsubscribe

typealias Unsubscribe = () -> Unit

fun Node.dispose(): Node =
    takeOr(unsubscribe == null && children.isEmpty()) {
        children.values.forEach { it.dispose() }
        unsubscribe?.invoke()
        copy(children = mapOf(), unsubscribe = null)
    }
