package org.d3if0110.miniproject.model

import androidx.compose.ui.graphics.vector.ImageVector
import java.time.LocalDate

data class Note(
    val keterangan: String,
    val nominal: Float,
    val tanggal: LocalDate,
    val status: String
)

data class TabItem(
    val title: String,
    val unSelectedIcon: ImageVector,
    val selectedIcon: ImageVector
)
