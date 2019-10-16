package com.zoffl.qureno.demo

import android.view.View
import android.widget.TextView
import com.zoffl.qureno.app.BaseState
import com.zoffl.qureno.demo.CounterDemo.*
import qureno.components.Component
import qureno.components.component
import qureno.core.Action
import qureno.core.Dispatch

class CounterDemo {

    data class State(
        val counter: Int = 0
    ) : BaseState

    class Increase : Action
    class Decrease : Action
}

fun counterComponent(): Component<State> =
    component(R.layout.counter, View::update, State::reduce)

private fun State.reduce(action: Action) =
    when (action) {
        is Increase -> copy(counter = counter + 1)
        is Decrease -> copy(counter = counter - 1)
        else -> this
    }

private fun View.update(state: State, dispatch: Dispatch) {
    findViewById<TextView>(R.id.counter).text = state.counter.toString()
    findViewById<View>(R.id.increase).setOnClickListener { dispatch(Increase()) }
    findViewById<View>(R.id.decrease).setOnClickListener { dispatch(Decrease()) }
}
