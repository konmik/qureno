package qureno.components

import qureno.core.Action

typealias Effect<T> = ViewNode.(state: T, action: Action) -> Unit

fun <T> emptyEffect(): Effect<T> = { _, _ -> }

fun <T> UpdateViewNode<T>.plusEffect(effect: Effect<T>): UpdateViewNode<T> =
    { state, action ->
        this@plusEffect(state, action).apply {
            effect(state, action)
        }
    }

fun <T : Any> subscribeToUpdateViewNode(tag: String, subscribe: Subscribe<T>): UpdateViewNode<T?> =
    { state, _ ->
        if (state != null && tag !in unsubscribe) {
            copy(unsubscribe = unsubscribe + (tag to subscribe(state, dispatch)))
        } else if (state == null && tag in unsubscribe) {
            unsubscribe[tag]?.invoke()
            copy(unsubscribe = unsubscribe - tag)
        } else {
            this
        }
    }
