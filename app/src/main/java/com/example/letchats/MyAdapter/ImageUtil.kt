package com.example.letchats.MyAdapter

import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.letchats.R



    @BindingAdapter("bindImageSVGorPNG")
    fun bindImageForSVG(view: AppCompatImageView, imageURL: String?) {
        Log.d("ImageUtilsAdapter", "imageUrl >>>: $imageURL")
        val requestBuilder: RequestBuilder<PictureDrawable> = Glide.with(view.context)
            .`as`(PictureDrawable::class.java)
            .placeholder(R.drawable.ic_drawable_upload)

        if (imageURL != null) {
            if (imageURL.isNotEmpty() && !imageURL.isBlank()) {
                if (imageURL.contains(".svg")) {
                    val uri: Uri = Uri.parse(imageURL)
                    requestBuilder.load(uri).into(view)
                } else
                    bindMenuImageFromUrl(view, imageURL)
            }
        }
    }

    @BindingAdapter("menuImageFromUrl")
    fun bindMenuImageFromUrl(view: ImageView, imageUrl: String?) {
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(view.context)
                .load(imageUrl)
                .dontAnimate()
                .placeholder(R.drawable.ic_drawable_upload)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                // .skipMemoryCache(true)
                //.transition(withCrossFade())
                .into(view)
        } else
        {


        }        }//view.setImageDrawable(
           // ContextCompat.getDrawable(
             //   view.context,
            //    R.drawable.ic_drawable_upload
          //  )
      //  )
   // }




