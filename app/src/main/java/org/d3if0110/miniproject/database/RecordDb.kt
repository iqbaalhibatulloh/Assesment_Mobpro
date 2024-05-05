package org.d3if0110.miniproject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3if0110.miniproject.model.Record

@Database(entities = [Record::class], version = 1, exportSchema = false)
abstract class RecordDb : RoomDatabase() {

    abstract val dao: RecordDao
    companion object {

        @Volatile
        private var INSTANCE: RecordDb? = null

        fun getInstance(context: Context): RecordDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RecordDb::class.java,
                        "record.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}