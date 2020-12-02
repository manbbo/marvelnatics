package br.com.digitalhouse.marvelnaticos.marvelnatics.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SeriesSummary (@SerializedName("resourceURI") @Expose var resourceURI: String,
                          @SerializedName("name") @Expose var name: String) {
}