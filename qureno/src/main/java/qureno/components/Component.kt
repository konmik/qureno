package qureno.components

import qureno.core.*

class Component<T>(
    val reduce: Reduce<T>,
    val updateNode: UpdateNode<T>,
    val updateViewNode: UpdateViewNode<T>
)

fun <P, T> Component<T>.focus(get: P.() -> T, plus: P.(T) -> P): Component<P> =
    Component(reduce.focus(get, plus), updateNode.focus(get), updateViewNode.focus(get))

fun <T : Any> Component<T>.opt(): Component<T?> =
    Component(reduce.opt(), updateNode.opt(), updateViewNode.opt())

fun <T> Component<T>.plusReduce(reduce: Reduce<T>): Component<T> =
    Component(combineReduce(arrayOf(this.reduce, reduce)), updateNode, updateViewNode)

fun <T> Component<T>.plusEffect(effect: Effect<T>): Component<T> =
    Component(reduce, updateNode.plusEffect(effect), updateViewNode)

fun <T> Component<T>.plusSubscribe(subscribe: Subscribe<T>): Component<T> =
    Component(reduce, updateNode.plusSubscribe(subscribe), updateViewNode)

fun <T> Component<T>.plusViewEffect(viewEffect: ViewEffect<T>): Component<T> =
    Component(reduce, updateNode, updateViewNode.plusViewEffect(viewEffect))
