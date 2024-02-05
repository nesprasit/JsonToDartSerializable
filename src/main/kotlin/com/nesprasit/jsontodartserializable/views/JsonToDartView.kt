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

import com.intellij.json.JsonFileType
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.util.ui.JBDimension
import com.intellij.util.ui.JBUI
import com.nesprasit.jsontodartserializable.extensions.isJSON
import com.nesprasit.jsontodartserializable.extensions.toPretty
import com.nesprasit.jsontodartserializable.helpers.GsonHelper
import java.awt.Dimension
import java.awt.Font
import java.awt.event.ActionEvent
import javax.swing.*

enum class ViewType {
    FILE, GENERATE
}

class JsonToDartView(private val dialog: DialogWrapper,val viewType: ViewType) {
    private lateinit var fileNameField: JTextField
    private lateinit var classNameField: JTextField
    private lateinit var jsonEditor: Editor

    fun createCenterPanel(): JComponent = JPanel().apply {
        this.preferredSize = Dimension(1024, 600)
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        setBorder(JBUI.Borders.empty(0, 16, 16, 16))

        add(JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            add(JLabel("Please input the JSON String and class name to generate Dart data class").apply {
                font = Font(font.name, Font.PLAIN, 14)
            })
            add(Box.createHorizontalGlue())
        })

        add(Box.createRigidArea(Dimension(0, 8)))
        if(viewType == ViewType.FILE){
            add(buildFieldFileName())
            add(Box.createRigidArea(Dimension(0, 8)))
        }
        add(buildFieldClassName())
        add(Box.createRigidArea(Dimension(0, 8)))
        add(JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            add(JLabel("JSON Text : ").apply {
                font = Font(font.name, Font.BOLD, 14)
            })
            add(Box.createHorizontalGlue())
        })
        add(Box.createRigidArea(Dimension(0, 8)))
        add(buildJSONEditor().component)
        add(Box.createRigidArea(Dimension(0, 8)))
        add(JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            add(Box.createHorizontalGlue())
            add(buildFormatButton())
        })
    }

    private fun buildFieldFileName(): JPanel {
        return JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            add(JLabel("File Name : ").apply {
                font = Font(font.name, Font.BOLD, 14)
            })

            fileNameField = JTextField()
            add(fileNameField.apply {
                maximumSize = JBDimension(5000, 40)
                addActionListener {
                    checkGenerateButtonEnabled()
                }
                document?.addDocumentListener(object : DocumentListener, javax.swing.event.DocumentListener {
                    override fun insertUpdate(e: javax.swing.event.DocumentEvent?) {
                        checkGenerateButtonEnabled()
                    }

                    override fun removeUpdate(e: javax.swing.event.DocumentEvent?) {
                        checkGenerateButtonEnabled()
                    }

                    override fun changedUpdate(e: javax.swing.event.DocumentEvent?) {
                        checkGenerateButtonEnabled()
                    }
                })
            })
        }
    }

    private fun buildFieldClassName(): JPanel {
        return JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            add(JLabel("Class Name : ").apply {
                font = Font(font.name, Font.BOLD, 14)
            })
            classNameField = JTextField()
            add(classNameField.apply {
                maximumSize = JBDimension(5000, 40)
                addActionListener {
                    checkGenerateButtonEnabled()
                }
                document?.addDocumentListener(object : DocumentListener, javax.swing.event.DocumentListener {
                    override fun insertUpdate(e: javax.swing.event.DocumentEvent?) {
                        checkGenerateButtonEnabled()
                    }

                    override fun removeUpdate(e: javax.swing.event.DocumentEvent?) {
                        checkGenerateButtonEnabled()
                    }

                    override fun changedUpdate(e: javax.swing.event.DocumentEvent?) {
                        checkGenerateButtonEnabled()
                    }
                })
            })
        }
    }

    private fun buildFormatButton(): JButton = JButton("Format").apply {
        addActionListener(object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent?) {
                val jsonText = jsonEditor.document.text
                GsonHelper.newInstance().prettyFormat(
                    jsonText,
                    run = { _ ->
                        checkGenerateButtonEnabled()
                    },
                    err = {
                        dialog.isOKActionEnabled = false
                    },
                )
            }
        })
    }

    private fun buildJSONEditor(): Editor {
        val editorFactory = EditorFactory.getInstance()
        val document = editorFactory.createDocument("").apply {
            setReadOnly(false)
            addDocumentListener(object : DocumentListener {
                override fun documentChanged(event: DocumentEvent) {
                    checkGenerateButtonEnabled()
                }
            })
        }

        val editor = editorFactory.createEditor(document, null, JsonFileType.INSTANCE, false)
        editor.component.apply {
            isEnabled = true
            autoscrolls = true
        }

        jsonEditor = editor
        return editor
    }

    private fun checkGenerateButtonEnabled() {
        var fileName = true
        val className = classNameField.text.isNotEmpty()
        val jsonText = getJSONText()

        if(viewType == ViewType.FILE) {
            fileName = fileNameField.text.isNotEmpty()
        }

        dialog.isOKActionEnabled = fileName && className && jsonText.isJSON()
    }

    fun getFileName(): String = fileNameField.text?.trim() ?: ""

    fun getClassName(): String = classNameField.text?.trim() ?: ""

    fun getJSONText(): String = jsonEditor.document.text.trim().toPretty()
}