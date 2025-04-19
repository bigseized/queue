package ru.bigseized.queue.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import ru.bigseized.queue.core.ResultOfRequest
import ru.bigseized.queue.data.api.QueueApi
import ru.bigseized.queue.data.api.UserApi
import ru.bigseized.queue.data.dataBase.QueueDAO
import ru.bigseized.queue.data.dataBase.UserDAO
import ru.bigseized.queue.domain.DTO.QueueDTO
import ru.bigseized.queue.domain.DTO.UserDTO
import ru.bigseized.queue.domain.model.Queue
import ru.bigseized.queue.domain.model.User
import javax.inject.Inject

@HiltViewModel
class QueueScreenViewModel @Inject constructor(
    private val queueApi: QueueApi,
    private val queueDAO: QueueDAO,
    private val userApi: UserApi,
    private val userDAO: UserDAO,
) : ViewModel() {

    private val _resultOfStarting: MutableStateFlow<ResultOfRequest<Queue>?> =
        MutableStateFlow(null)
    val resultOfStarting: StateFlow<ResultOfRequest<Queue>?> = _resultOfStarting

    private val _resultOfDeleting: MutableStateFlow<ResultOfRequest<Unit>?> = MutableStateFlow(null)
    val resultOfDeleting: StateFlow<ResultOfRequest<Unit>?> = _resultOfDeleting

    fun starting(id: String) {
        viewModelScope.launch {
            val resultOfRequest = queueApi.getQueue(id)
            _resultOfStarting.update { resultOfRequest }
        }
    }

    fun deleteQueue(currQueue: QueueDTO) {
        viewModelScope.launch {
            val currUser: User = userDAO.getCurrUser()!!
            val currUserDTO = UserDTO(currUser.username)
            var resultOfRequest1: ResultOfRequest<Unit> = ResultOfRequest.Loading
            var resultOfRequest2: ResultOfRequest<Unit> = ResultOfRequest.Loading
            launch {
                queueDAO.deleteQueue(currQueue.id)
            }
            val job1 = launch {
                resultOfRequest1 = userApi.deleteQueueFromUser(currQueue)
            }
            val job2 = launch {
                resultOfRequest2 = queueApi.deleteUserFromQueue(currUserDTO, currQueue.id)
            }
            launch {
                var name = ""
                for (queue in currUser.queues) {
                    if (queue.id == currQueue.id) {
                        name = queue.name
                        break
                    }
                }
                currUser.queues.remove(QueueDTO(currQueue.id, name))
                userDAO.updateUser(currUser)
            }

            job1.join()
            job2.join()
            if (resultOfRequest1 is ResultOfRequest.Success && resultOfRequest2 is ResultOfRequest.Success) {
                _resultOfDeleting.update {
                    ResultOfRequest.Success(Unit)
                }
            } else {
                _resultOfDeleting.update {
                    ResultOfRequest.Error("some problems")
                }
            }
        }
    }
}