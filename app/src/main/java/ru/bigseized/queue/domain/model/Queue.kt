package ru.bigseized.queue.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.bigseized.queue.domain.DTO.UserDTO

@Entity(tableName = "queues")
data class Queue(
    @PrimaryKey
    var id: String = "",
    val name: String = "Queue",
    @TypeConverters(ListOfUserToJsonConverter::class)
    val users: MutableList<UserDTO> = mutableListOf(),
    val allUsersAreAdmins: Boolean = false,
)