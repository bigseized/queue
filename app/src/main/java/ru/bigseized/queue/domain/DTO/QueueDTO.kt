package ru.bigseized.queue.domain.DTO

data class QueueDTO(
    val id: String = "",
    val name: String = "",
    var admin: Boolean = false,
)