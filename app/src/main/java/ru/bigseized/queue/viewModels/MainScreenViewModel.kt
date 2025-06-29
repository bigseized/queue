package ru.bigseized.queue.viewModels

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.bigseized.queue.core.NotificationService
import ru.bigseized.queue.core.ResultOfRequest
import ru.bigseized.queue.data.api.UserApi
import ru.bigseized.queue.domain.DTO.QueueDTO
import ru.bigseized.queue.domain.model.User
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val userApi: UserApi,
) : ViewModel() {

    private val _resultOfStarting: MutableStateFlow<ResultOfRequest<List<QueueDTO>>?> =
        MutableStateFlow(null)
    val resultOfStarting: StateFlow<ResultOfRequest<List<QueueDTO>>?> = _resultOfStarting

    fun starting(context: Context) {
        if (_resultOfStarting.value == null) {
            val intent = Intent(context, NotificationService::class.java)
            Log.d("notify", "before service start")
            context.startService(intent)
            Log.d("notify", "after service start")
            viewModelScope.launch(Dispatchers.IO) {
                userApi.startListeningQueues(auth.currentUser!!.uid) { user: User? ->
                    _resultOfStarting.value = ResultOfRequest.Success(user!!.queues)
                }
            }
        }
    }

}