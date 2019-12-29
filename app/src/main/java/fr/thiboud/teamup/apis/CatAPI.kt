package fr.thiboud.teamup.apis

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.Json
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

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
    fun getImage(@Query("breed_id") breedId: String?): Deferred<List<Image>>
}

data class Breeds(
    val id: String?,
    val name: String?,
    val description: String?
): Parcelable {

    val imgURL: String = """https://api.thecatapi.com/v1/images/search?api_key=$CAT_API_KEY&breed_id=$id"""

    private val _image = MutableLiveData<Image>()
    val image: LiveData<Image>
        get() = _image

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    fun setImg(img: Image) {
        _image.value = img
    }

    suspend fun loadImage() {
        val breedImageDeferred = CatAPI.retrofitService.getImage(this.id)
        try {
            this.setImg(breedImageDeferred.await()[0])
        } catch (e: Exception) {

        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Breeds> {
        override fun createFromParcel(parcel: Parcel): Breeds {
            return Breeds(parcel)
        }

        override fun newArray(size: Int): Array<Breeds?> {
            return arrayOfNulls(size)
        }
    }
}


data class Image(
    val id: String,
    @Json(name = "url") val imageURL: String,
    val width: String,
    val height: String
)