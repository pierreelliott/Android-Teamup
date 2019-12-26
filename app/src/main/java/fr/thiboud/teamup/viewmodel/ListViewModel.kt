package fr.thiboud.teamup.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.thiboud.teamup.apis.Breeds
import fr.thiboud.teamup.apis.CatAPI

import fr.thiboud.teamup.database.UserDao
import fr.thiboud.teamup.model.User
import kotlinx.coroutines.*

class ListViewModel(
    private val database: UserDao,
    application: Application,
    private val userID: Long = 0L // userID
) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )
    private val imgLoaderScope = CoroutineScope(Job() + Dispatchers.Main)


    private val _listItems = MutableLiveData<List<Breeds>>()
    val listItems: LiveData<List<Breeds>>
        get() = _listItems

    private var loadedItems: List<Breeds> = ArrayList()

    init {
        Log.i("ListViewModel", "created")
        coroutineScope.launch {
            getBreeds()
        }
    }

    private suspend fun getBreeds() {
        val breedsDeferred = CatAPI.retrofitService.getBreeds()
        try {
            val listResult = breedsDeferred.await()
            _listItems.value = listResult
            //getBreedsImage() // Populate breeds images
//                _listItems.value = "Success: ${listResult.size} Mars properties retrieved"
        } catch (e: Exception) {
//                _listItems.value = ArrayList()
//                _listItems.value = "Failure: ${e.message}"
        }
    }

    private suspend fun getBreedsImage() {
        withContext(Dispatchers.IO) {
            loadedItems.forEach {
                imgLoaderScope.launch {
                    val breedImageDeferred = CatAPI.retrofitService.getImage(it.id)
                    try {
                        it.setImg(breedImageDeferred.await())
//                        _listItems.value?.add(it)
                    } catch (e: Exception) {

                    }
                }

            }
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

    override fun onCleared() {
        super.onCleared()
        Log.i("LoginViewModel", "destroyed")
        viewModelJob.cancel()
    }
}
