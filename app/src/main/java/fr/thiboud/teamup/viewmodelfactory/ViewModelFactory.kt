package fr.thiboud.teamup.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.thiboud.teamup.apis.Breeds
import fr.thiboud.teamup.database.UserDao
import fr.thiboud.teamup.viewmodel.ItemViewModel
import fr.thiboud.teamup.viewmodel.ListViewModel
import fr.thiboud.teamup.viewmodel.LoginViewModel
import fr.thiboud.teamup.viewmodel.SubscriptionViewModel

class ViewModelFactory (private val application: Application) : ViewModelProvider.Factory {

    private lateinit var dataSource: UserDao
    private var userID: Long = 0L // userID

    private lateinit var breed: Breeds

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(dataSource, application, userID) as T // userID
        }
        if (modelClass.isAssignableFrom(SubscriptionViewModel::class.java)) {
            return SubscriptionViewModel(dataSource, application) as T
        }
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(dataSource, application) as T
        }
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            return ItemViewModel(application, breed) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    constructor(dataSource: UserDao, application: Application, userID: Long = 0L) : this(application) {
        this.dataSource = dataSource
        this.userID = userID
    }

    constructor(application: Application, breeds: Breeds) : this(application) {
        this.breed = breeds
    }
}