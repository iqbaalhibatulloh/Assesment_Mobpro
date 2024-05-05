package org.d3if0110.miniproject.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material.icons.outlined.Payment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.d3if0110.miniproject.database.RecordDao
import org.d3if0110.miniproject.model.Record
import org.d3if0110.miniproject.model.TabItem

class MainViewModel(dao: RecordDao) : ViewModel() {

    companion object {
        val tabItems = listOf(
            TabItem(
                title = "Pemasukan",
                unSelectedIcon = Icons.Outlined.Money,
                selectedIcon = Icons.Filled.Money
            ),
            TabItem(
                title = "Pengeluaran",
                unSelectedIcon = Icons.Outlined.Payment,
                selectedIcon = Icons.Filled.Payment
            )
        )
    }

    val data: StateFlow<List<Record>> = dao.getNote().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}
