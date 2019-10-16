package com.zoffl.qureno.app

import com.zoffl.qureno.demo.*
import qureno.components.Component

typealias StateClassName = String

/**
 * All screens must be registered here.
 *
 * Instead of taking advantage of inheritance and dynamic dispatch based on virtual functions, we're using this map.
 * Flows of data and dependencies are different and they must not me mixed,
 * however we need to match data types and functions in this specific case.
 */
val componentMap: Map<StateClassName, Lazy<Component<*>>> = mapOf(
    componentEntry { rootScreenComponent() },
    componentEntry { counterComponent() },
    componentEntry { effectDemoComponent() },
    componentEntry { subscriptionDemoComponent() },
    componentEntry { verticalDemoComponent() }
)

inline fun <reified T : BaseState> componentEntry(noinline getComponent: () -> Component<T>): Pair<StateClassName, Lazy<Component<T>>> =
    T::class.java.name to lazy(getComponent)
