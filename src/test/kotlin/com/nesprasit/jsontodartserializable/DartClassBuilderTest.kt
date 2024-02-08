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
package com.nesprasit.jsontodartserializable

import com.nesprasit.jsontodartserializable.extensions.toPascalCase
import com.nesprasit.jsontodartserializable.extensions.toPretty
import com.nesprasit.jsontodartserializable.generates.DartClassBuilder
import com.nesprasit.jsontodartserializable.generates.DartClassGenerator
import org.junit.jupiter.api.Test
import java.io.File

class DartClassBuilderTest {

    @Test
    fun build() {
        val json = readJSON("json3.json").toPretty()
        if(json.isNotEmpty()){
            val generated = DartClassGenerator("DartEntity", json).generate()
            val build = DartClassBuilder("dart_entity", generated)
            println(build.build())
        }
    }

    @Test
    fun toPascalCase() {
        val text = "emp_email_Name".toPascalCase()
        println("text $text")
    }

    @Suppress("SameParameterValue")
    private fun readJSON(name: String): String {
        var rootPath = System.getProperty("user.dir")
        rootPath += "/src/test/kotlin/"
        rootPath += this::class.java.packageName.replace(".", "/")
        val jsonFile = File("${rootPath}/src/$name")
        return jsonFile.readText()
    }

}