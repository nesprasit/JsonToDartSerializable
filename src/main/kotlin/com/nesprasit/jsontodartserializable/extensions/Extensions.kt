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
package com.nesprasit.jsontodartserializable.extensions

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory
import com.jetbrains.lang.dart.DartFileType
import com.nesprasit.jsontodartserializable.helpers.GsonHelper
import java.util.regex.Pattern

fun String.isJSON(): Boolean {
    return try {
        val jsonElement = Gson().fromJson(this, JsonElement::class.java)
        return jsonElement.isJsonObject || jsonElement.isJsonArray
    } catch (e: Exception) {
        false
    }
}

fun String.toLowerCamelCase(): String {
    return when {
        Character.isLowerCase(this[0]) -> this
        else -> StringBuilder().append(Character.toLowerCase(this[0]).plus(this.substring(1))).toString()
    }
}

fun String.toUpperCamelCase(): String {
    return when {
        isEmpty() -> ""
        Character.isUpperCase(this[0]) -> this
        else -> StringBuilder().append(Character.toUpperCase(this[0]).plus(this.substring(1))).toString()
    }
}

fun String.toPascalCase(): String {
    val linePattern = Pattern.compile("_([a-z0-9A-Z])")
    val matcher = linePattern.matcher(this)
    val sb = StringBuffer()
    while (matcher.find()) {
        matcher.appendReplacement(sb, matcher.group(1).uppercase())
    }
    matcher.appendTail(sb)
    return sb.toString()
}

fun String.addNullSafety(): String {
    return "$this?"
}

fun String.toPretty() = GsonHelper.newInstance().pretty(this)

fun JsonPrimitive.toTypeName(): String {
    return when {
        this.isBoolean -> "bool"
        this.isNumber -> when {
            this.asString.contains(".") -> "double"
            this.asLong > Integer.MAX_VALUE -> "int"
            else -> "int"
        }

        this.isString -> "String"
        else -> "String"
    }
}

fun JsonArray.isJsonObjectAll(): Boolean {
    return this.map { it.isJsonObject }.contains(true)
}

fun JsonArray.isJsonPrimitiveAll(): Boolean {
    return this.map { it.isJsonPrimitive }.contains(true)
}

fun String.isJSONObject(): Boolean {
    val jsonElement = Gson().fromJson(this, JsonElement::class.java)
    return jsonElement.isJsonObject
}

fun String.isJSONArray(): Boolean {
    val jsonElement = Gson().fromJson(this, JsonElement::class.java)
    return jsonElement.isJsonArray
}

fun Project.executeCommandAction(action: (Project) -> Unit) {
    CommandProcessor.getInstance().executeCommand(this, {
        ApplicationManager.getApplication().runWriteAction { action.invoke(this) }
    }, "JSON to Dart Class", "JSON to Dart Class")
}

fun Project.createDartFile(fileName: String, directory: PsiDirectory, contentClass: String){
    val psiFileFactory = PsiFileFactory.getInstance(this)
    val file = psiFileFactory.createFileFromText("$fileName.dart", DartFileType.INSTANCE, contentClass)
    directory.add(file)
}