package com.zoffl.qureno.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zoffl.qureno.demo.R
import com.zoffl.qureno.util.stateFromJson
import com.zoffl.qureno.util.stateToJson
import qureno.components.Component
import qureno.core.Action
import qureno.core.NoAction
import java.util.*

interface BaseState // base state type is required for Moshi parser

inline fun <reified T : BaseState> Context.startComponentActivity(state: T, initAction: Action = NoAction) {
    startComponentActivity(state, initAction, T::class.java.name)
}

class ComponentActivity : AppCompatActivity() {

    private lateinit var viewId: String
    private lateinit var engine: QurenoEngine<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.component_activity)
        viewId = savedInstanceState?.getString(VIEW_ID_KEY) ?: UUID.randomUUID().toString()
        engine = engines.getOrPut(viewId) { createEngine() }
        engine.onCreateView(findViewById(R.id.component_activity_container))
    }

    private fun createEngine(): QurenoEngine<*> {
        val start = intent?.extras?.getString(START_SCREEN_KEY)?.stateFromJson<StartScreen>()
        require(start != null) { "Use startComponentActivity to start this activity" }
        @Suppress("UNCHECKED_CAST")
        val component = componentMap.getValue(start.componentId).value as Component<BaseState>
        return QurenoEngine(start.state, component).apply {
            dispatch(start.initAction)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(VIEW_ID_KEY, viewId)
    }

    override fun onDestroy() {
        super.onDestroy()
        engine.onDestroyView()
        if (isFinishing) {
            engine.destroy()
            engines.remove(viewId)
        }
    }
}

private val engines: MutableMap<String, QurenoEngine<*>> = mutableMapOf()

private const val START_SCREEN_KEY = "startScreen"
private const val VIEW_ID_KEY = "viewId"

@PublishedApi
internal fun < T : BaseState> Context.startComponentActivity(state: T, initAction: Action, componentId: String) {
    val start = StartScreen(state, initAction, componentId)
    val intent = Intent(this, ComponentActivity::class.java)
        .putExtra(START_SCREEN_KEY, start.stateToJson())
    startActivity(intent)
}

private class StartScreen(val state: BaseState, val initAction: Action, val componentId: String)
