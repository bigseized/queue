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
import ru.bigseized.queue.data.dataBase.UserDAO
import ru.bigseized.queue.domain.model.User
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val userApi: UserApi,
    private val userDao: UserDAO,
) : ViewModel() {

    private val _result: MutableStateFlow<ResultOfRequest<User?>?> = MutableStateFlow(null)
    val result: StateFlow<ResultOfRequest<User?>?> = _result

    fun starting() {
        viewModelScope.launch(Dispatchers.IO) {
            if (auth.currentUser != null) {
                val resultOfRequest = userApi.getUser(auth.currentUser!!.uid)
                if (resultOfRequest is ResultOfRequest.Success) {
                    userDao.updateUser(resultOfRequest.result!!)
                }
                _result.update { resultOfRequest }
            } else {
                _result.update { ResultOfRequest.Error("no sign in") }
            }
        }
    }

}