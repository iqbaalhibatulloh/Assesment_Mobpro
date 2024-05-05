package org.d3if0110.miniproject.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if0110.miniproject.database.RecordDao
import org.d3if0110.miniproject.model.Record
import java.util.Date

class DetailViewModel(private val dao: RecordDao) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun insert(
        keterangan: String,
        nominal: Float,
        status: String
        ) {
        val statusFilter = when (status) {
            "Pemasukan", "Income" -> "Pemasukan"
            "Pengeluaran", "Expense" -> "Pengeluaran"
            else -> ""
        }

        val record = Record(
            keterangan = keterangan,
            nominal = if(statusFilter == "Pemasukan") nominal else nominal * -1,
            tanggal = Date().time,
            status = statusFilter
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(record)
        }
    }

    suspend fun getNote(id: Long): Record? {
        return dao.getNoteById(id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun update(
        id: Long,
        keterangan: String,
        nominal: Float,
        status: String
    ) {
        val statusFilter = when (status) {
            "Pemasukan", "Income" -> "Pemasukan"
            "Pengeluaran", "Expense" -> "Pengeluaran"
            else -> ""
        }
        val record = Record(
            id      = id,
            keterangan = keterangan,
            nominal = if(statusFilter == "Pemasukan") nominal else nominal * -1,
            tanggal = Date().time,
            status = statusFilter
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(record)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}