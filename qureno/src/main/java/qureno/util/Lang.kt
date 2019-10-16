package qureno.util

import org.jetbrains.annotations.Contract
import java.util.Collections.singletonMap

@PublishedApi
@Contract(pure = true)
internal inline fun <T> T.takeOr(predicate: (T) -> Boolean, update: T.() -> T): T =
    takeOr(predicate(this), update)

@PublishedApi
@Contract(pure = true)
internal inline fun <T> T.takeOr(take: Boolean, update: T.() -> T): T =
    if (take) this else update(this)

internal fun <K, T> mapOfSingle(key: K, value: T): Map<K, T> =
    singletonMap(key, value)
