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

import com.nesprasit.jsontodartserializable.extensions.addNullSafety
import com.nesprasit.jsontodartserializable.extensions.toUpperCamelCase
import com.nesprasit.jsontodartserializable.model.GenericDartClass
import com.nesprasit.jsontodartserializable.model.ListDartClass
import com.nesprasit.jsontodartserializable.model.DartClass

class DartClassBuilder(private val fileName: String, private val dartClass: DartClass) {
    private val import = "import 'package:json_annotation/json_annotation.dart';"
    private val serializable = "@JsonSerializable()"

    fun build(): String {
        val build = StringBuilder()
        build.append(import)
        build.append("\n")
        build.append("\n")
        build.append(part(fileName))
        build.append("\n")
        build.append("\n")
        build.append(buildClass(dartClass))
        build.append("\n")
        return build.toString()
    }

    private fun buildClass(dartClass: DartClass): String {
        val build = StringBuilder()
        if (dartClass.name.isNotEmpty()) {
            build.append(serializable)
            build.append("\n")
            build.append("class ${dartClass.name.toUpperCamelCase()} {")
            build.append("\n")
            build.append(buildContentClass(dartClass))
            build.append("}")
            build.append("\n")
            build.append("\n")
        }

        dartClass.referencedClasses.forEach {
            if (it is GenericDartClass) {
                build.append(buildClass(it))
            }
            if (it is ListDartClass && it.referencedClass.referencedClasses.isNotEmpty()) {
                build.append(buildClass(it.referencedClass))
            }
        }
        return build.toString()
    }

    private fun buildContentClass(dartClass: DartClass): String {
        val name = dartClass.name.toUpperCamelCase()
        val build = StringBuilder()
        build.append(buildField(dartClass))
        build.append("\n")
        build.append(buildConstructor(dartClass))
        build.append("\n")
        build.append("\n")
        build.append(fromJson(name))
        build.append("\n")
        build.append("\n")
        build.append(toJson(name))
        build.append("\n")
        return build.toString()
    }

    private fun buildField(dartClass: DartClass): String {
        val build = StringBuilder()
        dartClass.referencedClasses.forEach {
            var classes: DartClass = it
            if (it is GenericDartClass) {
                classes = it
            }
            if (it is ListDartClass) {
                classes = it.referencedClasses.single()
            }

            build.append("  ${jsonKey(classes.originalName)}")
            build.append("\n")
            build.append("  final ${classes.typeName.addNullSafety()} ")
            build.append("${classes.name};")
            build.append("\n")
        }
        return build.toString()
    }

    private fun buildConstructor(dartClass: DartClass): String {
        val build = StringBuilder()
        build.append("  ${dartClass.name.toUpperCamelCase()} ({")
        build.append("\n")
        dartClass.referencedClasses.forEach {
            var classes: DartClass = it
            if (it is GenericDartClass) {
                classes = it
            }
            if (it is ListDartClass) {
                classes = it.referencedClasses.single()
            }

            build.append("    this.${classes.name}")
            build.append(",")
            build.append("\n")
        }
        build.append("  });")
        return build.toString()
    }

    private fun part(name: String): String {
        return "part '$name.g.dart';"
    }

    private fun jsonKey(name: String): String {
        return "@JsonKey(name: \"${name}\")"
    }

    private fun fromJson(name: String): String {
        return "  factory ${name}.fromJson(Map<String, dynamic> json) {\n" +
                "    return _\$${name}FromJson(json);\n" +
                "  }"
    }

    private fun toJson(name: String): String {
        return "  Map<String, dynamic> toJson() {\n" +
                "    return _\$${name}ToJson(this);\n" +
                "  }"
    }
}