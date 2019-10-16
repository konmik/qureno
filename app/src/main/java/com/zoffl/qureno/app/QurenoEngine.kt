package com.zoffl.qureno.app

import android.widget.FrameLayout
import qureno.basic.frameComponent
import qureno.components.Component
import qureno.components.Node
import qureno.components.ViewNode
import qureno.components.dispose
import qureno.core.NoAction
import qureno.core.OnReduce
import qureno.core.Store

class QurenoEngine<T>(initialState: T, component: Component<T>) {

    private val containerComponent = frameComponent(component)

    private val onReduce: OnReduce<T> = { state, action ->
        node = containerComponent.updateNode(node, state, action)
        viewNode = viewNode?.(containerComponent.updateViewNode)(state, action)
    }

    private val store = Store(initialState, containerComponent.reduce, onReduce)
    private var node = Node(store.dispatch)
    private var viewNode: ViewNode? = null

    val dispatch = store.dispatch

    fun onCreateView(container: FrameLayout) {
        viewNode = ViewNode(store.dispatch, container.context, container)
        store.dispatch(NoAction) // dispatch an empty action to draw
    }

    fun onDestroyView() {
        viewNode = null
    }

    fun destroy() {
        node.dispose()
    }
}
