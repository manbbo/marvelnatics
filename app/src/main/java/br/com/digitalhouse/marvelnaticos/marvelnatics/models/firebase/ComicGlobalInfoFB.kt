package br.com.digitalhouse.marvelnaticos.marvelnatics.models.firebase

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class ComicGlobalInfoFB(@Exclude var comicID: Int, var ratingAverage: Double, var ratingUsers: Int, var ratingTotal: Int, var totals: MutableMap<String, Int>) {
    constructor() : this(0, 0.0, 0, 0, mutableMapOf())
}