package org.d3if0110.miniproject.model

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class Record (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val keterangan: String,
    val nominal: Float,
    val tanggal: Long,
    val status: String
)

data class TabItem(
    val title: String,
    val unSelectedIcon: ImageVector,
    val selectedIcon: ImageVector
)
