package com.nesprasit.jsontodartserializable.tests

import com.nesprasit.jsontodartserializable.extensions.toPascalCase
import com.nesprasit.jsontodartserializable.extensions.toPretty
import com.nesprasit.jsontodartserializable.generates.DartClassBuilder
import com.nesprasit.jsontodartserializable.generates.DartClassGenerator
import org.junit.jupiter.api.Test
import java.io.File

class DartClassBuilderTest {

    @Test
    fun build() {
        val json = readJSON("json7.json").toPretty()
        val generated = DartClassGenerator("DartEntity", json).generate()
        val build = DartClassBuilder("dart_entity", generated)
        println(build.build())
    }

    @Test
    fun toPascalCase() {
        val text = "emp_email_Name".toPascalCase()
        println("text $text")
    }

    @Suppress("SameParameterValue")
    private fun readJSON(name: String): String {
        var rootPath = System.getProperty("user.dir")
        rootPath += "/src/main/kotlin/"
        rootPath += this::class.java.packageName.replace(".", "/")
        val jsonFile = File("${rootPath}/$name")
        return jsonFile.readText()
    }

}