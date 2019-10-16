package qureno.components

import android.view.View
import qureno.core.*

/*
Component construction functions for generic View types.
 */

inline fun <T, reified V : View> componentTyped(
    layoutId: Int,
    noinline update: UpdateTyped<T, V>? = null,
    noinline reduce: Reduce<T>? = null,
    noinline effect: Effect<T>? = null
): Component<T> =
    componentTyped(
        create = inflateCreateTyped(layoutId),
        update = update,
        reduce = reduce,
        effect = effect
    )

inline fun <T, reified V : View> componentTyped(
    noinline create: CreateTyped<T, V>,
    noinline update: UpdateTyped<T, V>? = null,
    noinline reduce: Reduce<T>? = null,
    noinline effect: Effect<T>? = null,
    noinline viewEffect: ViewEffect<T>? = null
): Component<T> =
    component(
        reduce = reduce,
        updateNode = effect?.let(::effectToUpdateNode),
        updateViewNode = updateViewNodeTyped(create, update, viewEffect)
    )
