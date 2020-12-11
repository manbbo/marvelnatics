package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.digitalhouse.marvelnaticos.marvelnatics.api.Credentials
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Comic
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Data
import br.com.digitalhouse.marvelnaticos.marvelnatics.services.Repository
import br.com.digitalhouse.marvelnaticos.marvelnatics.util.Utils.Companion.hashFormat
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class BuscaViewModel(val repository: Repository) : ViewModel() {

    val listComics = MutableLiveData<ArrayList<Comic>>()
    var credentials = Credentials()
    var totRes = MutableLiveData<Int>()
    var offset = 0

    fun popListResult(query: String) {
        viewModelScope.launch {
            var date = Date().toString()
            var res = repository.getComics(
                    credentials.publicKey,
                    hashFormat(credentials.privateKey, credentials.publicKey, date),
                    date,
                    offset,
                    titleStartWith = query
            )

            offset++
            Log.i("BUSCA: ", offset.toString())
            if (listComics.value == null) listComics.value = res.data.results
            else listComics.value?.addAll(res.data.results)

            totRes.value = res.data.total
        }
    }
}