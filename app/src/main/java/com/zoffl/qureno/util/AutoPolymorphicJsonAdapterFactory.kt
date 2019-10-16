package com.zoffl.qureno.util

import com.squareup.moshi.*
import java.lang.reflect.Type

class AutoPolymorphicJsonAdapterFactory(private val baseClass: Class<*>, private val classNameKey: String) : JsonAdapter.Factory {

    override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? =
        if (baseClass != Types.getRawType(type)) null else Adapter(moshi).nullSafe()

    private inner class Adapter(private val moshi: Moshi) : JsonAdapter<Any>() {

        override fun fromJson(reader: JsonReader): Any {
            val findClassName = reader.peekJson().findClassName()
            return moshi.adapter(Class.forName(findClassName)).fromJson(reader)!!
        }

        override fun toJson(writer: JsonWriter, value: Any?) {
            require(value != null)
            writer.apply {
                beginObject()

                name(classNameKey).value(value::class.java.name)

                val flattenToken = beginFlatten()
                moshi.adapter<Any>(value::class.java).toJson(writer, value)
                endFlatten(flattenToken)

                endObject()
            }
        }

        private fun JsonReader.findClassName(): String {
            beginObject()
            while (hasNext()) {
                if (nextName() == classNameKey) {
                    return nextString().also { close() }
                } else {
                    skipValue()
                }
            }

            close()
            throw JsonDataException("Missing key $classNameKey")
        }
    }
}
