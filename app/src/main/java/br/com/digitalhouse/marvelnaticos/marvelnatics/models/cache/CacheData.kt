package br.com.digitalhouse.marvelnaticos.marvelnatics.models.cache

import java.io.Serializable

data class CacheData(var cacheDate: Long, var comics: Array<ComicCache>, var emDestaque: IntArray, var maisLidas: IntArray, var melhorAvaliadas: IntArray, var avaliacoes: HashMap<Int, Int>) :
    Serializable {
    constructor() : this(0, arrayOf(), IntArray(0), IntArray(0), IntArray(0), HashMap())
}