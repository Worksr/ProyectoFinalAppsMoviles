package com.example.apoyoescolar.ui.ModelsClass

// clases disponibles para apoyo
enum class ClassType {
    Español, Matemáticas, Ingles, Algebra, Física, Historia, Aritmetica
}

enum class GradeType{
    Priamria, Secundaria, Preparatoria, Universidad
}

data class Clases(
    val id: Int,
    val title: String,
    val type: ClassType,
    val grade: GradeType,
    val description: String
)

data class Coments(
    val id: Int = 0,
    val classID: Int,
    val firstName: String,
    val lastName: String,
    val type: ClassType,
    val grade: GradeType,
    val coment: String
)