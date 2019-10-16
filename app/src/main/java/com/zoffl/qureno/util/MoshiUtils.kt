package com.zoffl.qureno.util

import com.squareup.moshi.Moshi
import com.zoffl.qureno.app.BaseState
import qureno.core.Action

val stateMoshi: Moshi = Moshi.Builder()
    .add(AutoPolymorphicJsonAdapterFactory(Action::class.java, "class"))
    .add(AutoPolymorphicJsonAdapterFactory(BaseState::class.java, "class"))
    .add(Unit::class.java, UnitAdapter())
    .build()

inline fun <reified T : Any> T.stateToJson(): String =
    stateMoshi.adapter<T>(T::class.java).toJson(this)!!

inline fun <reified T : Any> String.stateFromJson(): T =
    stateMoshi.adapter<T>(T::class.java).fromJson(this)!!
