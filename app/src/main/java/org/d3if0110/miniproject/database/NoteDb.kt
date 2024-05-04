package org.d3if0110.miniproject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3if0110.miniproject.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDb : RoomDatabase() {

    abstract val dao: NoteDao
    companion object {

        @Volatile
        private var INSTANCE: NoteDb? = null

        fun getInstance(context: Context): NoteDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NoteDb::class.java,
                        "note.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}