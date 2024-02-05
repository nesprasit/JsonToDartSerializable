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
package com.nesprasit.jsontodartserializable.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.nesprasit.jsontodartserializable.extensions.executeCommandAction
import com.nesprasit.jsontodartserializable.generates.DartClassBuilder
import com.nesprasit.jsontodartserializable.generates.DartClassGenerator
import com.nesprasit.jsontodartserializable.views.JsonToDartGenerateView

class JsonToDartGenerateAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.project?.let { project ->
            val dialog = JsonToDartGenerateView(project)
            dialog.show()

            val view = dialog.view()
            val className = view.getClassName()
            val jsonText = view.getJSONText()

            if(dialog.isOK){
                val file = e.getData(PlatformDataKeys.VIRTUAL_FILE)
                val editor = e.getData(PlatformDataKeys.EDITOR_EVEN_IF_INACTIVE)
                val fileName = file?.name ?: return
                val document = editor?.document ?: return

                val generated = DartClassGenerator(className, jsonText).generate()
                val jsonTextBuilder = DartClassBuilder(fileName, generated).build()

                project.executeCommandAction {
                    document.insertString(0, jsonTextBuilder)
                }
            }

        }
    }
}