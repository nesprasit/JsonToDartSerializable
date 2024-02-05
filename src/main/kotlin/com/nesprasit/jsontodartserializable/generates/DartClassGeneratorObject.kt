package com.nesprasit.jsontodartserializable.generates

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.nesprasit.jsontodartserializable.extensions.toPascalCase
import com.nesprasit.jsontodartserializable.extensions.toTypeName
import com.nesprasit.jsontodartserializable.extensions.toUpperCamelCase
import com.nesprasit.jsontodartserializable.model.GenericDartClass
import com.nesprasit.jsontodartserializable.model.DartClass

class DartClassGeneratorObject(private val className: String, private val jsonObject: JsonObject) {

    fun generate(): GenericDartClass {
        val referencedClasses = mutableListOf<DartClass>()
        jsonObject.entrySet().forEach { (key, value: JsonElement) ->
            when {
                value.isJsonNull -> {
                    val name = key.toPascalCase()
                    referencedClasses.add(DartClass(name, key, "dynamic"))
                }

                value.isJsonPrimitive -> {
                    val typeName = value.asJsonPrimitive.toTypeName()
                    val name = key.toPascalCase()
                    referencedClasses.add(DartClass(name, key, typeName))
                }

                value.isJsonArray -> {
                    val generate = DartClassGeneratorArray(key, value.asJsonArray)
                    referencedClasses.add(generate.generate())
                }

                value.isJsonObject -> {
                    val name = key.toPascalCase()
                    val generator = DartClassGeneratorObject(name, value.asJsonObject)
                    val generated = generator.generate()
                    generated.originalName = key
                    generated.typeName = name.toUpperCamelCase()
                    referencedClasses.add(generated)
                }
            }
        }

        return GenericDartClass(name = className, referencedClasses = referencedClasses)
    }
}