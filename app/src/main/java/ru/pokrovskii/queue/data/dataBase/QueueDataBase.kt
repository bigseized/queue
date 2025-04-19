package ru.bigseized.queue.data.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.bigseized.queue.domain.model.ListOfUserToJsonConverter
import ru.bigseized.queue.domain.model.Queue
import javax.inject.Singleton

@Singleton
@Database(
    entities = [Queue::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ListOfUserToJsonConverter::class)
abstract class QueueDataBase : RoomDatabase() {

    abstract val queueDAO: QueueDAO

    companion object {
        const val DATABASE_NAME = "queues"
    }

}