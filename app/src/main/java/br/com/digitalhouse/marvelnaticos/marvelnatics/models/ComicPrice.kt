package br.com.digitalhouse.marvelnaticos.marvelnatics.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ComicPrice (@SerializedName("type") @Expose var type: String,
                       @SerializedName("price") @Expose var price: Float) {
}