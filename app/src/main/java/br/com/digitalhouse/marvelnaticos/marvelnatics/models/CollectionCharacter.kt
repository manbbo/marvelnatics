package br.com.digitalhouse.marvelnaticos.marvelnatics.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CollectionCharacter(@SerializedName("available") @Expose var quantAvailable: Int,
                                     @SerializedName("comics") @Expose var comics: List<ComicCharacter>)
