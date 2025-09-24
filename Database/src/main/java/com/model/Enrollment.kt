package model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "enrollments")
data class Enrollment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val courseId: String = "",
    val userId: String = "",
    val status: String = "enrolled"
)