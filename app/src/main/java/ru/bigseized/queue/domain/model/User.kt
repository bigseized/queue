package ru.bigseized.queue.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.bigseized.queue.domain.DTO.QueueDTO

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: Int = 0,
    val username: String = "",
    @TypeConverters(ListOfQueueToJsonConverter::class)
    val queues: MutableList<QueueDTO> = mutableListOf()
)
