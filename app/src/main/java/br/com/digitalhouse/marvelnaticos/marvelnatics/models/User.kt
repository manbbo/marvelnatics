package br.com.digitalhouse.marvelnaticos.marvelnatics.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class User (@SerializedName("email") @Expose var email : String,
            @SerializedName("name") @Expose var name: String,
            @SerializedName("password") @Expose var password : String,
            @SerializedName("photoURL") @Expose var photoURL: String,
            @SerializedName("emailVerified") @Expose var emailVerified: Boolean,
            @SerializedName("uid") @Expose var uid: Int) {
}