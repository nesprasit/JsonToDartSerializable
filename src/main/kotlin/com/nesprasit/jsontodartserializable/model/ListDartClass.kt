package com.nesprasit.jsontodartserializable.model

data class ListDartClass(
    val referencedClass: DartClass,
) : DartClass(referencedClasses = mutableListOf(referencedClass))