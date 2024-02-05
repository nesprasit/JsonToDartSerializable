package com.nesprasit.jsontodartserializable.views

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent

class JsonToDartFileView(project: Project) : DialogWrapper(project) {
    private val view = JsonToDartView(this, ViewType.FILE)

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