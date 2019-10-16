package qureno.core

typealias Reduce<T> = T.(Action) -> T

fun <T> emptyReduce(): Reduce<T> = { this }

fun <T : Any> Reduce<T>.opt(): Reduce<T?> =
    { action ->
        val state = this
        if (state == null) state else this@opt(state, action)
    }

fun <P, T> Reduce<T>.focus(get: P.() -> T, plus: P.(T) -> P): Reduce<P> =
    { action ->
        val old = get()
        val new = this@focus(old, action)
        if (old === new) this else plus(new)
    }

fun <T> combineReduce(reduces: Array<Reduce<T>>): Reduce<T> =
    CombinedReduce(ArrayList<Reduce<T>>().apply {
        reduces.forEach { if (it is CombinedReduce) addAll(it.reduces) else add(it) }
    }.toTypedArray())

/**
 * This optimization allows it to have shorted stacktrace and overall a bit better performance during reduction.
 * If all combinations are made using CombinedReduce at the end there will be a simple arrays of all reducers
 * with a single loop over it.
 */
private class CombinedReduce<T>(val reduces: Array<Reduce<T>>) : (T, Action) -> T {
    override fun invoke(state: T, action: Action): T =
        reduces.fold(state) { acc, reduce ->
            reduce(acc, action)
        }
}
