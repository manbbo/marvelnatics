package br.com.digitalhouse.marvelnaticos.marvelnatics.models.cache

import java.io.Serializable

data class CacheData(var cacheDate: Long, var comics: Array<ComicCache>, var emDestaque: IntArray, var maisLidas: IntArray, var melhorAvaliadas: IntArray) :
    Serializable {
    constructor() : this(0, arrayOf(), IntArray(0), IntArray(0), IntArray(0))
}