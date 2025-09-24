package model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val courseId: String = "",
    val userId: String = "",
    val rating: Float = 0f,
    val comment: String = ""
)