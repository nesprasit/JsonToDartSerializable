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
package com.nesprasit.jsontodartserializable.helpers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.intellij.openapi.application.runWriteAction

class GsonHelper {
    private val prettyGson: Gson = GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create()

    companion object {
        fun newInstance(): GsonHelper = GsonHelper()
    }

    fun pretty(jsonText: String): String {
        if (jsonText.isNotEmpty()) {
            val fromJson = prettyGson.fromJson(jsonText, JsonElement::class.java)
            return prettyGson.toJson(fromJson)
        }
        return ""
    }

    fun prettyFormat(jsonText: String, run: (json: String) -> Unit, err: () -> Unit) {
        if (jsonText.isNotEmpty()) {
            try {
                val toJson = pretty(jsonText)
                runWriteAction { run.invoke(toJson) }
            } catch (_: Exception) {
                err.invoke()
            }
        }
    }
}