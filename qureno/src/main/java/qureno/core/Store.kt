package qureno.core

class Store<T>(initialState: T, private val reduce: Reduce<T>, private val onReduce: OnReduce<T>) {

    private var state: T = initialState

    private val dispatchReduce: Dispatch = {
        state = state.reduce(it)
        onReduce(state, it)
    }

    val dispatch: Dispatch =
        dispatchReduce.withQueue()
}
