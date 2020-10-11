package qureno.components

import android.view.View
import qureno.core.*
import qureno.util.takeOr

typealias UpdateViewNode<T> = ViewNode.(state: T, action: Action) -> ViewNode

fun <T> updateViewNode(create: Create<T>, update: Update<T>? = null, effect: ViewEffect<T>? = null): UpdateViewNode<T> =
    updateViewNodeTyped(create, update, effect)

fun <T> emptyUpdateViewNode(): UpdateViewNode<T> =
    { _, _ -> this }

fun <P, T> UpdateViewNode<T>.focus(get: P.() -> T): UpdateViewNode<P> =
    { state, action -> this@focus(get(state), action) }

fun <T : Any> UpdateViewNode<T>.opt(): UpdateViewNode<T?> =
    { state, action ->
        if (state == null) {
            if (view == null) {
                this
            } else {
                copy(view = null, children = mapOf())
            }
        } else {
            this@opt(state, action)
        }
    }

fun <T> UpdateViewNode<T>.plusViewEffect(viewEffect: ViewEffect<T>): UpdateViewNode<T> =
    { state, action ->
        this@plusViewEffect(state, action).apply {
            view?.viewEffect(state, action, dispatch)
        }
    }

fun <T, V : View> updateViewNodeTyped(
    create: CreateTyped<T, V>,
    update: UpdateTyped<T, V>?,
    effect: ViewEffectTyped<T, V>?
): UpdateViewNode<T> =
    { state, action ->
        @Suppress("UNCHECKED_CAST")
        val new = view as V? ?: context?.let { create(state, dispatch, it) }
        (if (state != null) this else disposed())
            .takeOr(new == null || (view === new && boundValue === state)) {
                update?.invoke(new!!, state, dispatch)
                copy(view = new, boundValue = state)
            }.also {
                effect?.invoke(new!!, state, action, dispatch)
            }
    }

/**
 * Sometimes state gets recreated multiple times without changing it's value.
 * This optimization can help with such corner cases.
 */
fun <T> UpdateViewNode<T>.skipCompareByValue(): UpdateViewNode<T> =
    { state, action ->
        takeOr(boundValue === state) {
            if (boundValue == state) {
                copy(boundValue = state)
            } else {
                this@skipCompareByValue(state, action)
            }
        }
    }

internal fun <T> UpdateViewNode<T>.onViewAdded(f: View.() -> Unit): UpdateViewNode<T> =
    { state, action ->
        val new = this@onViewAdded(state, action)
        val newView = new.view
        new.takeOr(newView == null || newView === view) {
            copy(onViewAdded = { onViewAdded?.invoke(); f(newView!!) })
        }
    }

internal inline fun <T, reified V : View> UpdateViewNode<T>.onViewAddedTyped(noinline f: V.() -> Unit): UpdateViewNode<T> =
    onViewAdded {
        (this as V).f()
    }

fun <T> combineUpdateViewNode(vararg updates: UpdateViewNode<T>): UpdateViewNode<T> =
    { state, action ->
        updates.fold(this) { accumulated, update ->
            accumulated.update(state, action)
        }
    }

/*

forEach[T] @inline = fn { array :Array[T], acton :fn { item :T } ->
  loop i = 0, i < array.length() ? do {
    array(i).action()
    recur(i + 1)
  }
}

type FoldItem[T, R] = fn { accumulator :R, item :T, ::R }

fold[C, T, R] @inline = fn { collection :C, initial :R, forEach @auto :ForEach[C, T], operation :FoldItem[T, R], ::R ->
    accumulator @mutable = initial
    forEach(collection) { accumulator := operation(accumulator, a0) }
    return accumulator
}

array(1, 2, 3).fold(0) { accumulator + item }

*/
