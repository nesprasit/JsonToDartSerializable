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