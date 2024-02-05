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