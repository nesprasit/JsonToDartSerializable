package com.nesprasit.jsontodartserializable.model

open class DartClass(
    open var name: String = "",
    open var originalName: String = "",
    open var typeName: String = "",
    open var referencedClasses: List<DartClass> = listOf(),
)