package qureno.components

import qureno.core.*

class Component<T>(
    val reduce: Reduce<T>,
    val updateViewNode: UpdateViewNode<T>
)

fun <P, T> Component<T>.focus(get: P.() -> T, plus: P.(T) -> P): Component<P> =
    Component(reduce.focus(get, plus), updateViewNode.focus(get))

fun <T : Any> Component<T>.opt(): Component<T?> =
    Component(reduce.opt(), updateViewNode.opt())

fun <T> Component<T>.plusReduce(reduce: Reduce<T>): Component<T> =
    Component(combineReduce(arrayOf(this.reduce, reduce)), updateViewNode)

fun <T> Component<T>.plusEffect(effect: Effect<T>): Component<T> =
    Component(reduce, updateViewNode.plusEffect(effect))

fun <T : Any> Component<T>.plusSubscribe(tag: String, subscribe: Subscribe<T>): Component<T> =
    Component(reduce, combineUpdateViewNode(updateViewNode, subscribeToUpdateViewNode(tag, subscribe)))

fun <T> Component<T>.plusViewEffect(viewEffect: ViewEffect<T>): Component<T> =
    Component(reduce, updateViewNode.plusViewEffect(viewEffect))
