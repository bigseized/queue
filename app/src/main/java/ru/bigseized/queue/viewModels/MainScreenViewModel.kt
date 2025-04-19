package ru.bigseized.queue.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.bigseized.queue.core.ResultOfRequest
import ru.bigseized.queue.data.api.UserApi
import ru.bigseized.queue.domain.DTO.QueueDTO
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val userApi: UserApi
) : ViewModel() {

    private val _resultOfStarting: MutableStateFlow<ResultOfRequest<List<QueueDTO>>?> =
        MutableStateFlow(null)
    val resultOfStarting: StateFlow<ResultOfRequest<List<QueueDTO>>?> = _resultOfStarting

    fun starting(isNeedToUpdate: Boolean) {
        if (isNeedToUpdate || _resultOfStarting.value == null) {
            viewModelScope.launch(Dispatchers.IO) {

                val resultOfRequest = userApi.getUser(auth.currentUser!!.uid)
                if (resultOfRequest is ResultOfRequest.Success) {
                    _resultOfStarting.update {
                        ResultOfRequest.Success(resultOfRequest.result!!.queues)
                    }
                } else if (resultOfRequest is ResultOfRequest.Error) {
                    _resultOfStarting.update { resultOfRequest }
                }
            }
        }
    }

}