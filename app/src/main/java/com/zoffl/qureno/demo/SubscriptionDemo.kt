package com.zoffl.qureno.demo

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import com.zoffl.qureno.app.BaseState
import com.zoffl.qureno.app.app
import com.zoffl.qureno.demo.SubscriptionDemo.*
import com.zoffl.qureno.util.inflate
import com.zoffl.qureno.util.onClick
import qureno.components.*
import qureno.core.Action
import qureno.core.Dispatch

class SubscriptionDemo {
    data class State(
        val tick: Unit? = null
    ) : BaseState

    class DoSubscribe : Action
    class DoUnsubscribe : Action
}

// TODO: a simpler way to stack subscriptions
fun subscriptionDemoComponent(): Component<State> =
    component(
        reduce = State::reduce,
        updateNode = emptyUpdateNode<Unit>().plusSubscribe(::subscribe).opt().focus { tick },
        updateViewNode = updateViewNode(create = ::create, update = null, effect = null)
    )

fun State.reduce(action: Action): State =
    when (action) {
        is DoSubscribe -> copy(tick = Unit)
        is DoUnsubscribe -> copy(tick = null)
        else -> this
    }

private fun create(state: State, dispatch: Dispatch, context: Context): View =
    context.inflate(R.layout.demo_subscription).apply {
        onClick(R.id.subscribe) { dispatch(DoSubscribe()) }
        onClick(R.id.unsubscribe) { dispatch(DoUnsubscribe()) }
    }

private fun subscribe(state: Unit, dispatch: Dispatch): Unsubscribe {
    val handler = Handler(Looper.getMainLooper())
    var counter = 0
    val toastLoop = object : Runnable {
        override fun run() {
            Toast.makeText(app, "Subscription counter: ${counter++}", Toast.LENGTH_SHORT).show()
            handler.postDelayed(this, 1000)
        }
    }
    handler.post(toastLoop)
    return { handler.removeCallbacks(toastLoop) }
}
