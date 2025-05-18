package ru.buba.queue.domain.DTO

data class UserDTO(
    val id: String = "",
    var name: String = "",
    var admin: Boolean = false,
)