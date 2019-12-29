package fr.thiboud.teamup.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.thiboud.teamup.R

import fr.thiboud.teamup.database.UserDao
import fr.thiboud.teamup.model.User
import fr.thiboud.teamup.utils.Hash
import kotlinx.coroutines.*

class LoginViewModel(
    private val database: UserDao,
    application: Application,
    private val userID: Long = 0L // userID
) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    init {
        Log.i("LoginViewModel", "created")
        initializeUser()
    }

    private fun initializeUser() {
        uiScope.launch {
            _user.value = getUserFromDatabase()
        }
    }

    private suspend fun getUserFromDatabase(): User? {
        return withContext(Dispatchers.IO) {
            var user = database.get(userID) // userID
            if (user == null) {
                user = User()
            }
            user
        }
    }

    private suspend fun insert(user: User): Long {
        var id = 0L
        withContext(Dispatchers.IO) {
            id = database.insert(user)
        }
        return id
    }

    private suspend fun update(user: User) {
        withContext(Dispatchers.IO) {
            database.update(user)
        }
    }

    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // ================= Navigation ======================

    private val _loggedIn = MutableLiveData<User>()

    val loggedIn: LiveData<User>
        get() = _loggedIn

    fun doneNavigating() {
        _loggedIn.value = null
    }

    fun onValidateLogin() {
        uiScope.launch {
            val user = user.value ?: return@launch

            // TODO Alert "wrong login/password"
            if(user.login.isNullOrBlank() || user.password.isNullOrBlank()) {
                _error.value = getApplication<Application>().getString(R.string.ERR_LOGIN_WRONG_LOGIN_AND_PASSWORD)
                return@launch
            }

            val userDB = withContext(Dispatchers.IO) {
                val userDB = database.getByLogin(user.login!!)
                userDB
            }

            // FIXME Not secure
            user.password = Hash.md5(user.password!!)

            if(!user.password.equals(userDB?.password)) {
                _error.value = getApplication<Application>().getString(R.string.ERR_LOGIN_WRONG_LOGIN_AND_PASSWORD)
                _user.value?.password = ""
                return@launch
            }

            _loggedIn.value = user
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("LoginViewModel", "destroyed")
        viewModelJob.cancel()
    }
}
