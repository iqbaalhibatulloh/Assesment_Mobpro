package org.d3if0110.miniproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if0110.miniproject.model.Record

@Dao
interface RecordDao {

    @Insert
    suspend fun insert(record: Record)

    @Update
    suspend fun update(record: Record)

    @Query("SELECT * FROM note ORDER BY tanggal DESC")
    fun getNote(): Flow<List<Record>>

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteById(id: Long): Record?

    @Query("DELETE FROM note WHERE id = :id")
    suspend fun deleteById(id: Long)

}