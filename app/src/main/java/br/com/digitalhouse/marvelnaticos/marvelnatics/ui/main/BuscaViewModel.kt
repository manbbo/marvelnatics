package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.digitalhouse.marvelnaticos.marvelnatics.api.Credentials
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Comic
import br.com.digitalhouse.marvelnaticos.marvelnatics.services.Repository
import br.com.digitalhouse.marvelnaticos.marvelnatics.util.Utils.Companion.hashFormat
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class BuscaViewModel(val repository: Repository) : ViewModel() {

    val listComics = MutableLiveData<ArrayList<Comic>>()
    var credentials = Credentials()

    fun popListResult(query: String) {
        viewModelScope.launch {
            var date = Date().toString()
            var res = repository.getComics(
                    credentials.publicKey,
                    hashFormat(credentials.privateKey, credentials.publicKey, date),
                    date,
                    query
            )

            Log.i("VIEWMODEL", res.data.results.toString())
//
//            listComics.value = res.data.results
        }
    }
}