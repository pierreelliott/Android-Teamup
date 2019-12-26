package fr.thiboud.teamup.apis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

const val CAT_URL = "https://api.thecatapi.com/v1/"
const val CAT_API_KEY = "0a047a5a-df96-43d2-8caa-c9b4af473213"

private val retrofit = createRetrofitObject(CAT_URL)

object CatAPI {
    val retrofitService : MyCatApiService by lazy { retrofit.create(MyCatApiService::class.java) }
}

interface MyCatApiService {
    @Headers("""x-api-key: $CAT_API_KEY""")
    @GET("breeds")
    fun getBreeds(): Deferred<List<Breeds>>

    @Headers("""x-api-key: $CAT_API_KEY""")
    @GET("breeds")
    fun getBreeds(@Query("q") query: String): Deferred<List<Breeds>>

    @Headers("""x-api-key: $CAT_API_KEY""")
    @GET("images/search")
    fun getImage(@Query("breed_id") breedId: String): Deferred<Image>
}

data class Breeds(
    val id: String,
    val name: String,
    val description: String
) {

    val imgURL: String = """https://api.thecatapi.com/v1/images/search?api_key=$CAT_API_KEY&breed_id=$id"""

    private val _image = MutableLiveData<Image>()
    val image: LiveData<Image>
        get() = _image

    fun setImg(img: Image) {
        _image.value = img
    }

    suspend fun loadImage() {
        val breedImageDeferred = CatAPI.retrofitService.getImage(this.id)
        try {
            this.setImg(breedImageDeferred.await())
        } catch (e: Exception) {

        }
    }
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