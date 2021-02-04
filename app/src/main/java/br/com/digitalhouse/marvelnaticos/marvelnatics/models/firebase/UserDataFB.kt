package br.com.digitalhouse.marvelnaticos.marvelnatics.models.firebase

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserDataFB(@Exclude var userToken: String, var comicInfos: MutableMap<String, MutableList<Int>>) {

    constructor() : this("", mutableMapOf())

    fun isComicInList(comic: Int, list: String) = getComicIndexInList(comic, list) >= 0

    private fun getComicIndexInList(comic: Int, list: String) = comicInfos[list]?.indexOf(comic) ?: -1


    fun addComicInList(comic: Int, list: String): Boolean {
        if (isComicInList(comic, list)) return false
        if (comicInfos[list] == null) comicInfos[list] = mutableListOf()
        comicInfos[list]?.add(comic)
        return true
    }

    fun remComicFromList(comic: Int, list: String): Boolean {
        if (!isComicInList(comic, list)) return false
        comicInfos[list]?.remove(comic)
        if (comicInfos[list]?.size ?: 0 <= 0) comicInfos.remove(list)
        return true
    }
}