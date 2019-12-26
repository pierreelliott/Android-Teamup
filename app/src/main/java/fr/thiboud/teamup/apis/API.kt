package fr.thiboud.teamup.apis

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*


open class API(private val base_url: String) {

    private lateinit var lastCall: Date
    private val items: List<APIItem> = ArrayList()
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    protected open val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(base_url)
        .build()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    init {
        reload()
    }

    private fun canRecall() {
        val now = Calendar.getInstance()
        val diff = now.time.time - lastCall.time
    }

    fun reload() {
        val now = Calendar.getInstance()
        now.roll(Calendar.YEAR, -1)
        lastCall = now.time
    }

    suspend fun loadItems() {

    }

    suspend fun getItems(): List<APIItem> {
        if(items.isEmpty()) {
            withContext(Dispatchers.IO) {
                loadItems()
            }
        }

        return items
    }
}

open class APIItem(private val _name: String, private val _imageURL: String) {
    var imageURL: String?
        get() = _imageURL
        set(value) {}

    var name: String?
        get() = _name
        set(value) {}
}

fun createRetrofitObject(api_path: String): Retrofit {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(api_path)
        .build()

    return retrofit
}