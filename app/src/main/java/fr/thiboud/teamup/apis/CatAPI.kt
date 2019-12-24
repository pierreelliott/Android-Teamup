package fr.thiboud.teamup.apis

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

const val CAT_URL = "https://api.thecatapi.com/v1"
const val API_KEY = "0a047a5a-df96-43d2-8caa-c9b4af473213"

class CatAPI2: API(CAT_URL) {

    val retrofitService : MyApiService by lazy { retrofit.create(MyApiService::class.java) }

}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(CAT_URL)
    .build()

object CatAPI {
    val retrofitService : MyApiService by lazy { retrofit.create(MyApiService::class.java) }
}

interface MyApiService {
    @Headers("""x-api-key: $API_KEY""")
    @GET("breeds")
    fun getBreeds(): Deferred<List<Breeds>>

    @Headers("""x-api-key: $API_KEY""")
    @GET("images/search?breed_id={breedId}")
    fun getImage(@Path("breedId") breedId: String): Deferred<Image>
}

data class Breeds(
    val id: String,
    val name: String,
    val lifespan: String,
    val description: String
) {

    //val imgURL: String = """https://api.thecatapi.com/v1/images/search?api_key=$API_KEY&breed_id=$id"""
    var image: Deferred<Image> = CatAPI.retrofitService.getImage(id)
}



data class Image(
    val id: String,
    @Json(name = "url") val imageURL: String,
    val width: String,
    val height: String
)

//  =============== Viewmodel ================

//private fun getMarsRealEstateProperties() {
//    coroutineScope.launch {
//        val getPropertiesDeferred = MyApi.retrofitService.getProperties()
//        try {
//            val listResult = getPropertiesDeferred.await()
//            _response.value = "Success: ${listResult.size} Mars properties retrieved"
//        } catch (e: Exception) {
//            _response.value = "Failure: ${e.message}"
//        }
//    }
//}