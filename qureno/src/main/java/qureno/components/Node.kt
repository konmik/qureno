package qureno.components

import qureno.core.Dispatch
import qureno.util.takeOr

typealias Subscribe<T> = (state: T, dispatch: Dispatch) -> Unsubscribe

typealias Unsubscribe = () -> Unit

fun ViewNode.disposed(): ViewNode =
    takeOr(unsubscribe.isEmpty() && children.isEmpty()) {
        children.values.forEach { it.disposed() }
        unsubscribe.values.forEach { it() }
        copy(unsubscribe = mapOf(), children = mapOf())
    }

fun ViewNode.disposedView(): ViewNode =
    takeOr(view == null && children.isEmpty()) {
        children.mapValues { (_, value) -> value.disposedView() }
        copy(view = null)
    }
