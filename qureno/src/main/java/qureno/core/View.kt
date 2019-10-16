package qureno.core

import android.content.Context
import android.view.LayoutInflater
import android.view.View

typealias Create<T> = (state: T, Dispatch, Context) -> View

typealias Update<T> = View.(state: T, Dispatch) -> Unit

typealias ViewEffect<T> = View.(state: T, Action, Dispatch) -> Unit

typealias CreateTyped<T, V> = (state: T, Dispatch, Context) -> V

typealias UpdateTyped<T, V> = V.(state: T, Dispatch) -> Unit

typealias ViewEffectTyped<T, V> = V.(state: T, Action, Dispatch) -> Unit

fun <T> inflateCreate(layoutId: Int): Create<T> =
    inflateCreateTyped(layoutId)

inline fun <T, reified V : View> inflateCreateTyped(layoutId: Int): CreateTyped<T, V> =
    { _, _, context ->
        LayoutInflater.from(context).inflate(layoutId, null) as V
    }
