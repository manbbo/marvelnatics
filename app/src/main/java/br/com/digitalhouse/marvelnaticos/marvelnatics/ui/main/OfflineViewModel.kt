package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.digitalhouse.marvelnaticos.marvelnatics.database.AppDatabase
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Comic
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.db.ComicDB
import br.com.digitalhouse.marvelnaticos.marvelnatics.services.Repository
import kotlinx.coroutines.launch

class OfflineViewModel(val repository: Repository, val context: Context) : ViewModel() {
    val listComicsTenho = MutableLiveData<ArrayList<ComicDB?>>()
    val listComicsJaLi = MutableLiveData<ArrayList<ComicDB?>>()
    val listComicsQueroLer = MutableLiveData<ArrayList<ComicDB?>>()
    val listComicsFavoritos = MutableLiveData<ArrayList<ComicDB?>>()
    val listComics = MutableLiveData<ArrayList<ComicDB?>>()
    val listComicsSearch = MutableLiveData<ArrayList<ComicDB?>>()
    val listInfos = MutableLiveData<ArrayList<List<String>>>()
    val listInfosSearch = MutableLiveData<ArrayList<List<String>>>()
    val listInfo = MutableLiveData<ArrayList<String>>()
    val db = AppDatabase.invoke(context)

    fun popLists(){
        viewModelScope.launch {
            val listAllComicsClassifications = db.comicsDao().getAllComicsWithAllClassifications()

            listComicsTenho.value = ArrayList<ComicDB?>()
            listComicsJaLi.value = ArrayList<ComicDB?>()
            listComicsQueroLer.value = ArrayList<ComicDB?>()
            listComicsFavoritos.value = ArrayList<ComicDB?>()

            listAllComicsClassifications.forEach {
                val comicInfo = it
                it.infos.forEach {
                    if (it.info == "T") listComicsTenho.value!!.add(comicInfo.comic)
                    else if (it.info == "J") listComicsJaLi.value!!.add(comicInfo.comic)
                    else if (it.info == "Q") listComicsQueroLer.value!!.add(comicInfo.comic)
                    else listComicsFavoritos.value!!.add(comicInfo.comic)
                }
            }
        }
    }

    fun insertComicInList(id: Int, titulo: String, descricao: String, data: String, desenhistas: String, artistasCapa: String, criadores: String, urlImg: String, list: String){
        viewModelScope.launch {
            val comicDB = ComicDB(
                    null,
                    id,
                    titulo,
                    descricao,
                    data,
                    desenhistas,
                    artistasCapa,
                    criadores,
                    urlImg
            )
            db.comicsDao().insertComicDBList(comicDB, list)
        }
    }

    fun removeComicFromList(id: Int, titulo: String, descricao: String, data: String, desenhistas: String, artistasCapa: String, criadores: String, urlImg: String, list: String){
        viewModelScope.launch {
            val comicDB = ComicDB(
                    null,
                    id,
                    titulo,
                    descricao,
                    data,
                    desenhistas,
                    artistasCapa,
                    criadores,
                    urlImg
            )
            db.comicsDao().deleteComicList(comicDB, list)
        }
    }

   fun getClassificationsFromComic(apiId: Int){
       viewModelScope.launch {
           val comicClassif = db.comicsDao().getComicByApiIdWithAllClassifications(apiId)
           val listColecoes = ArrayList<String>()

           if(comicClassif.size > 0){
               comicClassif[0].infos.forEach {
                   listColecoes.add(it.info)
               }
           }

           listInfo.value = listColecoes
       }
    }

    fun getAllComics(){
        viewModelScope.launch {
            val listAllComics = ArrayList<ComicDB?>()
            val list = db.comicsDao().getAllComics()
            list.forEach {
                listAllComics.add(it)
            }
            listComics.value = listAllComics
        }
    }

    fun getAllInfos(){
        viewModelScope.launch {
            val listInfosComics = ArrayList<List<String>>()
            listComics.value!!.forEach {
                listInfosComics.add(db.comicsDao().getAllClassificationsFromComic(it!!.dbID!!))
            }
            listInfos.value = listInfosComics
        }
    }

    fun filterListByTitle(text: String){
        val comicsSearch = ArrayList<ComicDB?>()
        val infosSearch = ArrayList<List<String>>()
        for (i in 0..listComics.value!!.size-1) {
            if (listComics.value!![i]!!.titulo.contains(text)) {
                comicsSearch.add(listComics.value!![i])
                infosSearch.add(listInfos.value!![i])
            }
        }
        listComicsSearch.value = comicsSearch
        listInfosSearch.value = infosSearch
    }
}