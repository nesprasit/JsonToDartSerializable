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
package com.nesprasit.jsontodartserializable.views

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent

class JsonToDartGenerateView(project: Project) : DialogWrapper(project) {
    private val view = JsonToDartView(this, ViewType.GENERATE)

    init {
        init()
        title = "Generate Dart Data Class Code"
        setOKButtonText("Generate")
        isOKActionEnabled = false
    }

    override fun createCenterPanel(): JComponent {
        return view.createCenterPanel()
    }

    fun view(): JsonToDartView = view
}






