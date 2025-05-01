package ru.bigseized.queue.data.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.bigseized.queue.domain.model.ListOfQueueToJsonConverter
import ru.bigseized.queue.domain.model.User
import javax.inject.Singleton

@Singleton
@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ListOfQueueToJsonConverter::class)
abstract class UserDataBase : RoomDatabase() {

    abstract val userDAO: UserDAO

    companion object {
        const val DATABASE_NAME = "user"
    }

}