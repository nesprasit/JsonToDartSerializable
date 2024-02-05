package com.nesprasit.jsontodartserializable.generates

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.nesprasit.jsontodartserializable.extensions.isJSONArray
import com.nesprasit.jsontodartserializable.extensions.isJSONObject
import com.nesprasit.jsontodartserializable.model.GenericDartClass
import com.nesprasit.jsontodartserializable.model.ListDartClass
import com.nesprasit.jsontodartserializable.model.DartClass

class DartClassGenerator(private val className: String, private val json: String) {
    fun generate(): DartClass {
        return when {
            json.isJSONObject() -> generatorJSONObject()
            json.isJSONArray() -> generatorJSONArray()
            else -> throw IllegalStateException("Can't generate Dart Data Class from a no JSON Object/JSON Object Array")
        }
    }

    private fun generatorJSONObject(): GenericDartClass {
        val obj = Gson().fromJson(json, JsonObject::class.java)
        return DartClassGeneratorObject(className, obj).generate()
    }

    private fun generatorJSONArray(): ListDartClass {
        val obj = Gson().fromJson(json, JsonArray::class.java)
        return DartClassGeneratorArray(className, obj).generate()
    }
}