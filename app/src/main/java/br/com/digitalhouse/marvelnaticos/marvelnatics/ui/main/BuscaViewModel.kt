package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.ComicSearchAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.api.Credentials
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Comic
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Data
import br.com.digitalhouse.marvelnaticos.marvelnatics.services.Repository
import br.com.digitalhouse.marvelnaticos.marvelnatics.util.Utils.Companion.hashFormat
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class BuscaViewModel(val repository: Repository) : ViewModel() {

    val listComics = ArrayList<Comic?>()
    val mutableListComics = MutableLiveData<ArrayList<Comic?>>()
    var credentials = Credentials()
    var totRes = MutableLiveData<Int>()
    var isLoading = MutableLiveData<Boolean>(false)
    var offset = 0
    var adapterComics = MutableLiveData<ComicSearchAdapter>()

    fun popListResult(query: String) {
        viewModelScope.launch {
            isLoading.value = true
            adapterComics.value?.addNullData()

            var date = Date().toString()
            var res = repository.getComics(
                    credentials.publicKey,
                    hashFormat(credentials.privateKey, credentials.publicKey, date),
                    date,
                    offset,
                    titleStartWith = query
            )

            adapterComics.value?.removeNullData()

            isLoading.value = false
            if (offset == 0 || mutableListComics.value == null) mutableListComics.value = res.data.results
            else {
                mutableListComics.value?.addAll(res.data.results)
                adapterComics.value?.notifyItemRangeInserted(offset + 1, res.data.count)
            }

            offset += res.data.count

            totRes.value = res.data.total

        }
    }

    fun isLoading() = (isLoading.value == true)

    fun clearList(){
        offset = 0
        isLoading.value = false
        listComics.clear()
        mutableListComics.value = listComics
    }

    fun isLastPage() = (offset == totRes.value)
}