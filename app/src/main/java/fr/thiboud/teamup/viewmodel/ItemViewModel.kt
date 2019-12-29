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

class ItemViewModel(
    application: Application,
    breeds: Breeds
) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )


    private val _breed = MutableLiveData<Breeds>()
    val breed: LiveData<Breeds>
        get() = _breed


    init {
        Log.i("ListViewModel", "created")
        _breed.value = breeds
        coroutineScope.launch {
            _breed.value!!.loadImage()
        }
    }


//    private suspend fun getBreedsImage() {
//        withContext(Dispatchers.IO) {
//            loadedItems.forEach {
//                imgLoaderScope.launch {
//                    val breedImageDeferred = CatAPI.retrofitService.getImage(it.id)
//                    try {
//                        it.setImg(breedImageDeferred.await())
////                        _listItems.value?.add(it)
//                    } catch (e: Exception) {
//
//                    }
//                }
//
//            }
//        }
//    }

    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error


    override fun onCleared() {
        super.onCleared()
        Log.i("LoginViewModel", "destroyed")
        viewModelJob.cancel()
    }
}
