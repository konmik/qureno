package qureno.components

import qureno.core.*

/*
Component construction functions.
 */

fun <T> component(
    layoutId: Int,
    update: Update<T>? = null,
    reduce: Reduce<T>? = null,
    effect: Effect<T>? = null
): Component<T> =
    component(
        create = inflateCreate(layoutId),
        update = update,
        reduce = reduce,
        effect = effect
    )

fun <T> component(
    create: Create<T>,
    update: Update<T>? = null,
    reduce: Reduce<T>? = null,
    effect: Effect<T>? = null,
    viewEffect: ViewEffect<T>? = null
): Component<T> =
    component(
        reduce = reduce,
        updateNode = effect?.let(::effectToUpdateNode),
        updateViewNode = updateViewNode(create, update, viewEffect)
    )

fun <T> component(
    reduce: Reduce<T>? = null,
    updateNode: UpdateNode<T>? = null,
    updateViewNode: UpdateViewNode<T>? = null
): Component<T> =
    Component(
        reduce = reduce ?: emptyReduce(),
        updateNode = updateNode ?: emptyUpdateNode(),
        updateViewNode = updateViewNode ?: emptyUpdateViewNode()
    )
