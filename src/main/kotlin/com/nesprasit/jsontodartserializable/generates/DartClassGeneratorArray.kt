/*
 * Copyright (C) 2020 Nesprasit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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