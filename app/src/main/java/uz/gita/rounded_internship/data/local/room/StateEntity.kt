package uz.gita.rounded_internship.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "state")
data class StateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var state: Boolean = false
)
