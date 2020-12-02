package br.com.digitalhouse.marvelnaticos.marvelnatics.models
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ComicCharacter(@SerializedName("resourceURI") @Expose var resURI: String,
                          @SerializedName("name") @Expose var name: String)
