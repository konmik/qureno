package com.zoffl.qureno.demo

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import com.zoffl.qureno.app.BaseState
import com.zoffl.qureno.demo.SubscriptionDemo.*
import com.zoffl.qureno.util.inflate
import com.zoffl.qureno.util.onClick
import qureno.components.*
import qureno.core.Action
import qureno.core.Dispatch

class SubscriptionDemo {
    data class State(
        val counter: Int = 0,
        val tick: Unit? = null
    ) : BaseState

    class DoSubscribe : Action
    class DoUnsubscribe : Action
    class IncrementCounter : Action
}

// TODO: a simpler way to stack subscriptions
fun subscriptionDemoComponent(): Component<State> =
    component(
        reduce = State::reduce,
        updateViewNode = combineUpdateViewNode(
            updateViewNode(::create, View::update),
            subscribeToUpdateViewNode("tick", ::subscribe).focus { tick }
        )
    )

fun State.reduce(action: Action): State =
    when (action) {
        is DoSubscribe -> copy(tick = Unit)
        is DoUnsubscribe -> copy(tick = null)
        is IncrementCounter -> copy(counter = counter + 1)
        else -> this
    }

private fun create(state: State, dispatch: Dispatch, context: Context): View =
    context.inflate(R.layout.demo_subscription).apply {
        onClick(R.id.subscribe) { dispatch(DoSubscribe()) }
        onClick(R.id.unsubscribe) { dispatch(DoUnsubscribe()) }
    }

private fun View.update(state: State, dispatch: Dispatch) {
    findViewById<TextView>(R.id.counter).text = state.counter.toString()
}

private fun subscribe(state: Unit, dispatch: Dispatch): Unsubscribe {
    val handler = Handler(Looper.getMainLooper())
    val toastLoop = object : Runnable {
        override fun run() {
            dispatch(IncrementCounter())
            handler.postDelayed(this, 1000)
        }
    }
    handler.post(toastLoop)
    return { handler.removeCallbacks(toastLoop) }
}
