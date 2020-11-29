package br.com.digitalhouse.marvelnaticos.marvelnatics.models
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Character(@SerializedName("id") @Expose var id: Int,
                     @SerializedName("name") @Expose var name: String,
                     @SerializedName("description") @Expose var description: String,
                     @SerializedName("thumbnail") @Expose var thumbnail: Image,
                     @SerializedName("comics") @Expose var collection: CollectionCharacter)