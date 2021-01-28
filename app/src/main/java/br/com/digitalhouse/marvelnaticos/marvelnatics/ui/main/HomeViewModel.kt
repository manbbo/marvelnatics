package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.digitalhouse.marvelnaticos.marvelnatics.api.Credentials
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Comic
import br.com.digitalhouse.marvelnaticos.marvelnatics.services.Repository
import br.com.digitalhouse.marvelnaticos.marvelnatics.util.Utils
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class HomeViewModel(val repository: Repository) : ViewModel() {

    val listComics = MutableLiveData<ArrayList<Comic?>>()
    var credentials = Credentials()
    var totRes = MutableLiveData<Int>()
    var offset = 0

    fun popListResult(query: String) {
        viewModelScope.launch {
            try {
                var date = Date().toString()
                var res = repository.getComics(
                    credentials.publicKey,
                    Utils.hashFormat(credentials.privateKey, credentials.publicKey, date),
                    date,
                    offset,
                    titleStartWith = query
                )

                offset++
                listComics.value = res.data.results
                totRes.value = res.data.total
            } catch (ex: Exception) {
                Log.e("MainViewModel", "Erro!", ex)
            }
        }
    }
}