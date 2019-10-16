package com.zoffl.qureno.demo

import android.content.Context
import android.view.View
import com.zoffl.qureno.app.BaseState
import com.zoffl.qureno.app.startComponentActivity
import com.zoffl.qureno.util.inflate
import com.zoffl.qureno.util.onClick
import qureno.components.Component
import qureno.components.component
import qureno.core.Dispatch

class RootScreen {

    class State : BaseState

}

fun rootScreenComponent(): Component<RootScreen.State> =
    component(::create)

fun create(state: RootScreen.State, dispatch: Dispatch, context: Context): View =
    context.inflate(R.layout.demo_root_screen).apply {
        onClick(R.id.counter_demo) { context.startComponentActivity(CounterDemo.State()) }
        onClick(R.id.effect_demo) { context.startComponentActivity(EffectDemo.State()) }
        onClick(R.id.subscription_demo) { context.startComponentActivity(SubscriptionDemo.State()) }
        onClick(R.id.vertical_demo) { context.startComponentActivity(VerticalDemo.State()) }
    }
