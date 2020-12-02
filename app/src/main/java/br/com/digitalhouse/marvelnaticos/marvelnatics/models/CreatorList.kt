package br.com.digitalhouse.marvelnaticos.marvelnatics.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreatorList (@SerializedName("available") @Expose var available:Int,
                        @SerializedName("returned") @Expose var returned: Int,
                        @SerializedName("collectionURI") @Expose var collectionURI: String,
                        @SerializedName("items") @Expose var items: ArrayList<CharacterSummary>) {
}