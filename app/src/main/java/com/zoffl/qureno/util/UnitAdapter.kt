package com.zoffl.qureno.util

import com.squareup.moshi.*

class UnitAdapter: JsonAdapter<Unit>() {
    override fun fromJson(reader: JsonReader): Unit? =
        if (reader.peek() == JsonReader.Token.NULL) null else Unit

    override fun toJson(writer: JsonWriter, value: Unit?) {
        if (value == null ) writer.nullValue() else writer.value(1)
    }
}
