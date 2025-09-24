package com.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey val id: String = "",
    val title: String = "",
    val description: String = "",
    val rating: Double = 0.0,
    val ratingsCount: Int = 0,
    val isPopular: Boolean = false
)


