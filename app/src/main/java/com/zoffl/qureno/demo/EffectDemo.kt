package com.zoffl.qureno.demo

import android.content.Context
import android.view.View
import android.widget.Toast
import com.zoffl.qureno.app.BaseState
import com.zoffl.qureno.app.app
import com.zoffl.qureno.demo.EffectDemo.DoEffect
import com.zoffl.qureno.demo.EffectDemo.State
import com.zoffl.qureno.util.inflate
import com.zoffl.qureno.util.onClick
import qureno.components.Component
import qureno.components.ViewNode
import qureno.components.component
import qureno.core.Action
import qureno.core.Dispatch

class EffectDemo {

    class State : BaseState

    class DoEffect : Action
}

fun effectDemoComponent(): Component<State> =
    component(create = ::create, effect = ViewNode::effect)

private fun create(state: State, dispatch: Dispatch, context: Context): View =
    context.inflate(R.layout.demo_effect).apply {
        onClick(R.id.effect) { dispatch(DoEffect()) }
    }

private fun ViewNode.effect(state: State, action: Action) {
    Toast.makeText(app, "Effect", Toast.LENGTH_SHORT).show()
}
