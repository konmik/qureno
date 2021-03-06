package com.zoffl.qureno.demo

import android.content.Context
import android.graphics.Color
import android.widget.Button
import com.zoffl.qureno.app.BaseState
import com.zoffl.qureno.demo.VerticalDemo.*
import qureno.basic.verticalComponent
import qureno.components.*
import qureno.core.Action
import qureno.core.Dispatch
import qureno.util.background
import qureno.util.scopeActions

class VerticalDemo {

    data class State(
        val counter1: CounterDemo.State = CounterDemo.State(),
        val counter2: CounterDemo.State? = CounterDemo.State(),
        val counter3: CounterDemo.State = CounterDemo.State()
    ) : BaseState

    class Show : Action
    class Hide : Action
}

fun verticalDemoComponent(): Component<State> =
    verticalComponent(
        counterComponent().scopeActions(1).background(Color.GREEN).focus({ counter1 }, { copy(counter1 = it) }),
        counterComponent().scopeActions(2).background(Color.MAGENTA).opt().focus({ counter2 }, { copy(counter2 = it) }),
        counterComponent().scopeActions(3).background(Color.CYAN).focus({ counter3 }, { copy(counter3 = it) }),
        buttonComponent()
    )

private fun buttonComponent(): Component<State> =
    componentTyped(::create, Button::update, State::reduce)

private fun State.reduce(action: Action): State =
    when (action) {
        is Show -> copy(counter2 = CounterDemo.State())
        is Hide -> copy(counter2 = null)
        else -> this
    }

private fun create(state: State, dispatch: Dispatch, context: Context): Button =
    Button(context)

private fun Button.update(state: State, dispatch: Dispatch) {
    text = if (state.counter2 == null) "Create 2nd counter" else "Delete 2nd counter"
    setOnClickListener {
        dispatch(if (state.counter2 == null) Show() else Hide())
    }
}
