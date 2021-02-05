package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.digitalhouse.marvelnaticos.marvelnatics.ComicDBAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.ComicCollectionAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.database.AppDatabase
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Comic
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.db.ComicDB
import br.com.digitalhouse.marvelnaticos.marvelnatics.services.Repository
import com.google.android.gms.tasks.TaskCompletionSource
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.concurrent.thread

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
    var statistics = MutableLiveData<MutableMap<String, Int>>()


    fun popLists() {
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

    fun getStatistics() {
        viewModelScope.launch {
            statistics.value = HashMap<String, Int>().apply {
                put("Q", db.comicsDao().getComicInfo("Q"))
                put("T", db.comicsDao().getComicInfo("T"))
                put("J", db.comicsDao().getComicInfo("J"))
                put("F", db.comicsDao().getComicInfo("F"))
            }
        }
    }


    fun insertComicInList(
        id: Int,
        titulo: String,
        descricao: String,
        data: String,
        desenhistas: String,
        artistasCapa: String,
        criadores: String,
        urlImg: String,
        list: String
    ) = TaskCompletionSource<Nothing>().also { result ->
        viewModelScope.launch {
            try {
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
                result.setResult(null)
            } catch (ex: Exception) {
                Log.e("OfflineViewModel", "Erro!", ex)
                result.setException(ex)
            }
        }
    }.task

    fun removeComicFromList(
        id: Int,
        titulo: String,
        descricao: String,
        data: String,
        desenhistas: String,
        artistasCapa: String,
        criadores: String,
        urlImg: String,
        list: String
    ) {
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

    fun getClassificationsFromComic(apiId: Int) {
        viewModelScope.launch {
            val comicClassif = db.comicsDao().getComicByApiIdWithAllClassifications(apiId)
            val listColecoes = ArrayList<String>()

            if (comicClassif.size > 0) {
                comicClassif[0].infos.forEach {
                    listColecoes.add(it.info)
                }
            }

            listInfo.value = listColecoes
        }
    }

    fun getAllComics() = TaskCompletionSource<Nothing>().also { result ->
        viewModelScope.launch {
            try {
                val listAllComics = ArrayList<ComicDB?>()
                val list = db.comicsDao().getAllComics()
                list.forEach {
                    listAllComics.add(it)
                }
                listComics.value = listAllComics
                result.setResult(null)
            } catch (ex: Exception) {
                Log.e("OfflineViewModel", "Erro!", ex)
                result.setException(ex)
            }
        }
    }.task

    fun getAllInfos() = TaskCompletionSource<Nothing>().also { result ->
        viewModelScope.launch {
            try {
                val listInfosComics = ArrayList<List<String>>()
                listComics.value!!.forEach {
                    listInfosComics.add(
                        db.comicsDao().getAllClassificationsFromComic(it!!.dbID!!)
                    )
                }
                listInfos.value = listInfosComics
                result.setResult(null)
            } catch (ex: Exception) {
                Log.e("OfflineViewModel", "Erro!", ex)
                result.setException(ex)
            }
        }
    }.task

    fun filterListByTitle(text: String) {
        val comicsSearch = ArrayList<ComicDB?>()
        val infosSearch = ArrayList<List<String>>()
        for (i in 0..listComics.value!!.size - 1) {
            if (listComics.value!![i]!!.titulo.contains(text)) {
                comicsSearch.add(listComics.value!![i])
                infosSearch.add(listInfos.value!![i])
            }
        }
        listComicsSearch.value = comicsSearch
        listInfosSearch.value = infosSearch
    }

    fun updateComic(dbId: Long, apiId: Int, position: Int, adapter: ComicCollectionAdapter) {
        viewModelScope.launch {
            var updatedComicClassif =
                db.comicsDao().getComicByApiIdWithAllClassifications(apiId)

            if (listInfosSearch.value != null) {
                if (dbId != updatedComicClassif[0].comic.dbID) listComicsSearch.value!![position] =
                    updatedComicClassif[0].comic

                listInfosSearch.value!![position] = db.comicsDao()
                    .getAllClassificationsFromComic(updatedComicClassif[0].comic.dbID!!)

                var i = 0
                while (i < listComics.value!!.size && listComics.value!![i]!!.apiID != apiId) i++

                listComics.value!![i] = listComicsSearch.value!![position]
                listInfos.value!![i] = listInfosSearch.value!![position]
            } else {
                if (dbId != updatedComicClassif[0].comic.dbID) listComics.value!![position] =
                    updatedComicClassif[0].comic

                listInfos.value!![position] = db.comicsDao()
                    .getAllClassificationsFromComic(updatedComicClassif[0].comic.dbID!!)
            }

            adapter.notifyItemChanged(position)
        }
    }

    fun eraseComic(position: Int) {
        viewModelScope.launch {
            listInfos.value!!.removeAt(position)
            listComics.value!!.removeAt(position)
        }
    }

    fun updateInfosLists(
        countLists: List<Boolean>,
        dbId: Long,
        apiId: Int,
        adapters: List<ComicDBAdapter>
    ) {
        var mldComics: List<MutableLiveData<ArrayList<ComicDB?>>> =
            listOf(listComicsQueroLer, listComicsTenho, listComicsJaLi, listComicsFavoritos)

        viewModelScope.launch {
            var updatedComicClassif =
                db.comicsDao().getComicByApiIdWithAllClassifications(apiId)
            for (i in 0..countLists.size - 1) {
                var j = 0
                var found = false

                while (j < mldComics[i].value!!.size && mldComics[i].value!![j]!!.apiID != apiId) j++

                if (j < mldComics[i].value!!.size) found = true

                if (found != countLists[i]) {
                    if (found) {
                        mldComics[i].value!!.removeAt(j)
                        adapters[i].notifyItemRemoved(j)
                    } else {
                        mldComics[i].value!!.add(updatedComicClassif[0].comic)
                        adapters[i].notifyItemInserted(mldComics[i].value!!.size - 1)
                    }
                } else if (found) mldComics[i].value!![j] = updatedComicClassif[0].comic
            }
        }
    }

    fun clearDatabase() = TaskCompletionSource<Nothing>().also { result ->
        viewModelScope.launch {
            try {
                db.comicsDao().clearDatabase()
                result.setResult(null)
            } catch (ex: Exception) {
                Log.e("OfflineViewModel", "Erro!", ex)
                result.setException(ex)
            }
        }
    }.task


}
