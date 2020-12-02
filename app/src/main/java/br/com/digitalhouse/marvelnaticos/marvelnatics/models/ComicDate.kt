package br.com.digitalhouse.marvelnaticos.marvelnatics.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.text.DateFormat

data class ComicDate (@SerializedName("type") @Expose var type: String,
                      @SerializedName("date") @Expose var date: DateFormat) {
}