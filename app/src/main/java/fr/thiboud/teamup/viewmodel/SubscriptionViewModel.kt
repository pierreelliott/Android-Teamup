package fr.thiboud.teamup.viewmodel

import android.app.Application
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.thiboud.teamup.R
import fr.thiboud.teamup.database.UserDao
import fr.thiboud.teamup.model.User
import fr.thiboud.teamup.utils.Hash
import kotlinx.coroutines.*


class SubscriptionViewModel(
    private val database: UserDao,
    application: Application
) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    init {
        Log.i("SubscriptionViewModel", "created")
        initializeUser()
    }

    private fun initializeUser() {
        _user.value = User()
    }

    private suspend fun insert(user: User): Long {
        var id = 0L
        withContext(Dispatchers.IO) {
            id = database.insert(user)
        }
        return id
    }

    private val _subscribed = MutableLiveData<Long>()

    val subscribed: LiveData<Long>
        get() = _subscribed

    fun doneNavigating() {
        _subscribed.value = null
    }

    fun onValidateSubscription() {
        uiScope.launch {
            val user = user.value ?: return@launch

            if(user.login.isNullOrBlank() || user.password.isNullOrBlank() || user.lastname.isNullOrBlank()
                || user.firstname.isNullOrBlank() || user.email.isNullOrBlank() || user.address.isNullOrBlank()
                || user.city.isNullOrBlank() || user.country.isNullOrBlank()) {
                _error.value = getApplication<Application>().getString(R.string.ERR_SUBSCRIPTION_FIELDS_EMPTY)
                return@launch
            }

            if(loginExists(user.login!!)) {
                _error.value = getApplication<Application>().getString(R.string.ERR_SUBSCRIPTION_LOGIN_ALREADY_EXISTS)
                return@launch
            }

            if(!isEmailValid(user.email)) {
                _error.value = getApplication<Application>().getString(R.string.ERR_SUBSCRIPTION_EMAIL_FORMAT_INVALID)
                return@launch
            }

            user.password = Hash.md5(user.password!!)

            val userId = insert(user)

            _subscribed.value = userId
        }
    }

    private suspend fun loginExists(login: String): Boolean {
        return withContext(Dispatchers.IO) {
            val userDBExists = database.getByLogin(login) != null
            userDBExists
        }
    }

    private fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    val countryClicksListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            _user.value?.country = parent?.getItemAtPosition(position) as String
        }
    }

    val genderClicksListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            _user.value?.gender = parent?.getItemAtPosition(position) as String
        }
    }
}
