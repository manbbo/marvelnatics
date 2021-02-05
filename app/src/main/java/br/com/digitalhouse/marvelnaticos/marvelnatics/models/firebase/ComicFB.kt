package br.com.digitalhouse.marvelnaticos.marvelnatics.models.firebase

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class ComicFB(@Exclude var apiID: Int, var title: String, var description: String, var releaseDate: String, var coverUrl: String, var artistCreator: String, var artistDrawer: String, var artistCover: String, var rating: Map<String, Int>) {
    constructor() : this(0, "", "", "", "", "", "", "", mapOf())
}