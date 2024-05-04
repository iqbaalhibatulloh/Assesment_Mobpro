package org.d3if0110.miniproject.model

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val keterangan: String,
    val nominal: Float,
    val tanggal: String,
    val status: String
)

data class TabItem(
    val title: String,
    val unSelectedIcon: ImageVector,
    val selectedIcon: ImageVector
)
