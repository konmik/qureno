package com.zoffl.qureno.app

import android.widget.FrameLayout
import qureno.basic.frameComponent
import qureno.components.Component
import qureno.components.ViewNode
import qureno.components.disposed
import qureno.components.disposedView
import qureno.core.NoAction
import qureno.core.OnReduce
import qureno.core.Store

class QurenoEngine<T>(initialState: T, component: Component<T>) {

    private val containerComponent = frameComponent(component)

    private val onReduce: OnReduce<T> = { state, action ->
        viewNode = containerComponent.updateViewNode(viewNode, state, action)
    }

    private val store = Store(initialState, containerComponent.reduce, onReduce)
    private var viewNode: ViewNode = ViewNode(store.dispatch)

    val dispatch = store.dispatch

    fun onCreateView(container: FrameLayout) {
        viewNode = viewNode.copy(context = container.context, view = container)
        store.dispatch(NoAction) // dispatch an empty action to draw
    }

    fun onDestroyView() {
        viewNode = viewNode.disposedView()
    }

    fun destroy() {
        viewNode = viewNode.disposed()
    }
}
