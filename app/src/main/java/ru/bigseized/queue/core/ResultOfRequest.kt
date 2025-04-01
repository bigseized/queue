package ru.bigseized.queue.core

sealed class ResultOfRequest {
    class Success : ResultOfRequest()
    class Error(val errorMessage: String): ResultOfRequest()
}