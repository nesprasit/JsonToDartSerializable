package com.nesprasit.jsontodartserializable.generates

import com.google.gson.JsonArray
import com.nesprasit.jsontodartserializable.extensions.*
import com.nesprasit.jsontodartserializable.model.ListDartClass
import com.nesprasit.jsontodartserializable.model.DartClass

class DartClassGeneratorArray(private val className: String, private val jsonArray: JsonArray) {
    fun generate(): ListDartClass {
        val referencedClasses = mutableListOf<DartClass>()
        when {
            jsonArray.isJsonPrimitiveAll() -> {
                val single = jsonArray.first { !it.isJsonNull }
                val typeName = single?.asJsonPrimitive?.toTypeName() ?: ""
                val name = className.toPascalCase()
                val originalName = className
                referencedClasses.add(DartClass(name = name, originalName = originalName, typeName = "List<$typeName>"))
            }

            jsonArray.isJsonObjectAll() -> {
                val obj = jsonArray[0]
                val name = className.toPascalCase()
                val classes = DartClassGeneratorObject(name, obj.asJsonObject).generate()
                classes.originalName = className
                classes.typeName = "List<${name.toUpperCamelCase()}>"
                referencedClasses.add(classes)
            }

            else -> {
                val name = className.toPascalCase()
                val originalName = className
                referencedClasses.add(DartClass(name = name, originalName = originalName, typeName = "List<dynamic>"))
            }
        }

        return ListDartClass(referencedClass = referencedClasses.single())
    }
}