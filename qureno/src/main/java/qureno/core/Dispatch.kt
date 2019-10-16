package qureno.core

import java.util.*

typealias Dispatch = (Action) -> Unit

internal fun Dispatch.withQueue(): Dispatch {
    val queue = ArrayDeque<Action>()
    var isDispatchingAction: Action? = null

    return { action ->
        queue.add(action)

        if (isDispatchingAction == null) {
            while (!queue.isEmpty()) {
                queue.pop().let {
                    isDispatchingAction = it
                    try {
                        this@withQueue(it)
                    } finally {
                        isDispatchingAction = null
                    }
                }
            }
        }
    }
}
