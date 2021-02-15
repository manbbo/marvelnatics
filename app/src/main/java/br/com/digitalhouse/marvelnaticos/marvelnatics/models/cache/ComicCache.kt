package br.com.digitalhouse.marvelnaticos.marvelnatics.models.cache

import br.com.digitalhouse.marvelnaticos.marvelnatics.models.firebase.ComicFB
import java.io.Serializable

data class ComicCache(var apiID: Int, var title: String, var description: String, var releaseDate: String, var coverUrl: String, var artistCreator: String, var artistDrawer: String, var artistCover: String, var rating: Double) :
    Serializable {

    constructor() : this(0, "", "", "", "", "", "", "", 0.0)


    companion object {
        fun from(comic: ComicFB) = ComicCache(comic.apiID, comic.title, comic.description, comic.releaseDate, comic.coverUrl, comic.artistCreator, comic.artistDrawer, comic.artistCover, comic.rating.let { ratings ->
            var t = 0;
            var r = 0.0
            ratings.values.forEach { rate ->
                t++
                r += rate
            }
            r / t
        })
    }
}