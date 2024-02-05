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
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.nesprasit.jsontodartserializable.extensions.createDartFile
import com.nesprasit.jsontodartserializable.extensions.executeCommandAction
import com.nesprasit.jsontodartserializable.extensions.toPretty
import com.nesprasit.jsontodartserializable.generates.DartClassBuilder
import com.nesprasit.jsontodartserializable.generates.DartClassGenerator
import com.nesprasit.jsontodartserializable.views.JsonToDartFileView

class JsonToDartFileAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        e.project?.let { project ->
            getDirectory(e, project)?.let { directory ->
                val dialog = JsonToDartFileView(project)
                dialog.show()

                val view = dialog.view()
                val fileName = view.getFileName()
                val className = view.getClassName()
                val jsonText = view.getJSONText()

                if(dialog.isOK){
                    val generated = DartClassGenerator(className, jsonText).generate()
                    val jsonTextBuilder = DartClassBuilder(fileName, generated).build()

                    directory.project.executeCommandAction {
                        project.createDartFile(fileName, directory, jsonTextBuilder)
                    }
                }
            }
        }
    }


    private fun getDirectory(e: AnActionEvent, project: Project): PsiDirectory? {
        val context = e.dataContext
        val module = LangDataKeys.MODULE.getData(context) ?: return null

        return when (val navigationTable = LangDataKeys.NAVIGATABLE.getData(context)) {
            is PsiDirectory -> navigationTable
            is PsiFile -> navigationTable.containingDirectory
            else -> {
                val root = ModuleRootManager.getInstance(module)
                root.sourceRoots
                    .asSequence()
                    .mapNotNull { PsiManager.getInstance(project).findDirectory(it) }.firstOrNull()
            }
        }
    }
}