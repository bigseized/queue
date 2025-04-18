package ru.bigseized.queue.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val userDao: UserDAO
) : ViewModel() {

    private val _result: MutableStateFlow<ResultOfRequest<FirebaseUser?>?> = MutableStateFlow(null)
    val result: StateFlow<ResultOfRequest<FirebaseUser?>?> = _result

    fun starting() {
        viewModelScope.launch {
            // Checking user in DB
            //val currUser = userDao.getCurrUser()
            // If no user in DB we navigating screen to signUp
            if (auth.currentUser != null) {
                _result.value = ResultOfRequest.Success(auth.currentUser)
            } else {
                _result.value = ResultOfRequest.Error("no sign in")
            }
        }
    }

}