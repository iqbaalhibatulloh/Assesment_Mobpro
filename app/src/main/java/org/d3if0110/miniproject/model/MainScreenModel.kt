package org.d3if0110.miniproject.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class MainScreenModel : ViewModel() {
    private val _data = MutableLiveData<List<Note>>()
    val data: LiveData<List<Note>> = _data

    init {
        _data.value = getDummyData()
    }

    // Method untuk menambahkan catatan baru
    fun addNote(note: Note) {
        val currentNotes = _data.value.orEmpty().toMutableList()
        currentNotes.add(note)
        _data.value = currentNotes
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDummyData(): List<Note> {
    val dummyData = mutableListOf<Note>()

    // Tambahkan 10 data pemasukan
    for (i in 1..10) {
        val nominal = (i * 100).toFloat() + i
        val note = Note("Pemasukan $i", nominal, LocalDate.now(), "Pemasukan")
        dummyData.add(note)
    }

    // Tambahkan 10 data pengeluaran
    for (i in 1..10) {
        val nominal = -(i * 50).toFloat() + i
        val note = Note("Pengeluaran $i", nominal, LocalDate.now(), "Pengeluaran")
        dummyData.add(note)
    }

    return dummyData
}