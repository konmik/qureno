package com.zoffl.qureno.demo

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.zoffl.qureno.app.BaseState
import qureno.util.scopeActions
import qureno.basic.verticalComponent
import com.zoffl.qureno.demo.VerticalDemo.*
import qureno.components.*
import qureno.core.Action
import qureno.core.Dispatch

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
        counterComponent().scopeActions(1).focus({ counter1 }, { copy(counter1 = it) }),
        counterComponent().scopeActions(2).opt().focus({ counter2 }, { copy(counter2 = it) }),
        counterComponent().scopeActions(3).focus({ counter3 }, { copy(counter3 = it) }),
        buttonComponent()
    )

private fun buttonComponent(): Component<State> =
    component(::create, View::update, State::reduce)

private fun State.reduce(action: Action): State =
    when (action) {
        is Show -> copy(counter2 = CounterDemo.State())
        is Hide -> copy(counter2 = null)
        else -> this
    }

@SuppressLint("SetTextI18n")
private fun create(state: State, dispatch: Dispatch, context: Context): View =
    Button(context)

private fun View.update(state: State, dispatch: Dispatch) {
    require(this is TextView)
    text = if (state.counter2 == null) "Create 2nd counter" else "Delete 2nd counter"
    setOnClickListener {
        dispatch(if (state.counter2 == null) Show() else Hide())
    }
}
