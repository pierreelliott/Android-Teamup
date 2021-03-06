package fr.thiboud.teamup.utils

import android.net.Uri
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import fr.thiboud.teamup.R
import fr.thiboud.teamup.apis.Image

@BindingAdapter("breedImage")
fun ImageView.setBreedImage(image: Image?) {
    if(image != null) {
        val imgUri = image.imageURL.toUri().buildUpon().scheme("https").build()
        Glide.with(this.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image))
            .into(this)
    } else {
        Glide.with(this.context)
            .applyDefaultRequestOptions(RequestOptions().error(R.drawable.ic_broken_image))
            .clear(this)
    }
}

@BindingAdapter("testPlaceholder")
fun setPlaceholder(imageView: ImageView, img: String) {
    val imgUri = img.toUri().buildUpon().scheme("https").build()
    Glide.with(imageView.context)
        .load(imgUri)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
        .into(imageView)
//    setImageResource(when (item.gender) {
//        "Homme" -> R.mipmap.ic_man
//        else -> R.mipmap.ic_woman
//    })
}