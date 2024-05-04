package org.d3if0110.miniproject.model

import androidx.compose.ui.graphics.vector.ImageVector

data class Note(
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
