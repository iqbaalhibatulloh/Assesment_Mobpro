package org.d3if0110.miniproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if0110.miniproject.model.Note

@Dao
interface NoteDao {

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Query("SELECT * FROM note ORDER BY id DESC")
    fun getNote(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteById(id: Long): Note?

    @Query("DELETE FROM note WHERE id = :id")
    suspend fun deleteById(id: Long)

}