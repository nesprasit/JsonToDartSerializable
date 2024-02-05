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

            val file = e.getData(PlatformDataKeys.VIRTUAL_FILE)
            val editor = e.getData(PlatformDataKeys.EDITOR_EVEN_IF_INACTIVE)
            val fileName = file?.name ?: return
            val document = editor?.document ?: return

            val generated = DartClassGenerator(className, jsonText).generate()
            val jsonTextBuilder = DartClassBuilder(fileName, generated).build()
            println(jsonTextBuilder)


            project.executeCommandAction {
                document.insertString(0, jsonTextBuilder)
            }
        }
    }
}