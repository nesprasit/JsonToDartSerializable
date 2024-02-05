package com.nesprasit.jsontodartserializable.model

data class GenericDartClass(
    override var name: String,
    override var typeName: String = "",
    override var referencedClasses: List<DartClass> = listOf(),
) : DartClass(name = name, originalName = name, typeName = typeName, referencedClasses = referencedClasses)