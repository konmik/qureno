package qureno.components

import qureno.core.Action
import qureno.util.takeOr

typealias UpdateNode<T> = Node.(state: T, action: Action) -> Node

typealias Effect<T> = Node.(state: T, action: Action) -> Unit

fun <T> emptyUpdateNode(): UpdateNode<T> = { _, _ -> this }

fun <P, T> UpdateNode<T>.focus(get: P.() -> T): UpdateNode<P> =
    { state, action -> this@focus(get(state), action) }

fun <T> UpdateNode<T>.plusEffect(effect: Effect<T>): UpdateNode<T> =
    { state, action ->
        this@plusEffect(state, action).apply {
            effect(state, action)
        }
    }

fun <T> UpdateNode<T>.plusSubscribe(subscribe: Subscribe<T>): UpdateNode<T> =
    { state, action ->
        val newUnsubscribe = unsubscribe ?: subscribe(state, dispatch)
        val old = children[Unit]
        val new = this@plusSubscribe(old ?: Node(dispatch), state, action)
        takeOr(old === new && unsubscribe === newUnsubscribe) {
            copy(
                unsubscribe = newUnsubscribe,
                children = mapOf(Unit to new)
            )
        }
    }

fun <T> effectToUpdateNode(effect: Effect<T>): UpdateNode<T> =
    { state, action ->
        apply { effect(state, action) }
    }

fun <T : Any> UpdateNode<T>.opt(): UpdateNode<T?> =
    { state, action ->
        if (state == null) {
            dispose()
        } else {
            val old = children[Unit]
            val new = this@opt(old ?: Node(dispatch), state, action)
            takeOr(old === new) { copy(children = mapOf(Unit to new)) }
        }
    }

fun <T> UpdateNode<T>.focusChild(key: Any): UpdateNode<T> =
    { state, action ->
        val old = children[key]
        val new = this@focusChild(old ?: Node(dispatch), state, action)
        takeOr(old === new) {
            if (old?.unsubscribe != null && new.unsubscribe == null) {
                old.dispose()
            }
            copy(children = children + (key to new))
        }
    }

fun <T> combineUpdateNode(updates: Array<UpdateNode<T>>): UpdateNode<T> =
    CombinedUpdateNode(ArrayList<UpdateNode<T>>().apply {
        updates.forEach { if (it is CombinedUpdateNode) addAll(it.updates) else add(it) }
    }.toTypedArray())

/**
 * See CombinedReduce for explanation.
 */
private class CombinedUpdateNode<T>(val updates: Array<UpdateNode<T>>) : (Node, T, Action) -> Node {
    override fun invoke(node: Node, state: T, action: Action): Node =
        updates.fold(node) { acc, update ->
            acc.update(state, action)
        }
}
