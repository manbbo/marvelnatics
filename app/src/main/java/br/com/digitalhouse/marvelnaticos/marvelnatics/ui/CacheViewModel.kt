package br.com.digitalhouse.marvelnaticos.marvelnatics.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.cache.CacheData
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.launch
import java.io.*
import java.lang.Exception
import kotlin.concurrent.thread

class CacheViewModel : ViewModel() {

    val cacheData = MutableLiveData<CacheData>()

    fun loadData(context: Context, firebase: FirebaseViewModel?) {
        if (firebase == null || cacheData.value == null) loadDataFromCache(context)
        if (firebase != null) loadDataFromNetwork(firebase, context)
    }


    private fun loadDataFromNetwork(firebase: FirebaseViewModel, context: Context) = TaskCompletionSource<Nothing>().also { result ->
        thread {
            try {
                Tasks.await(firebase.loadMainDataForCache())?.also { data ->
                    viewModelScope.launch { cacheData.value = data }
                    saveData(context, data)
                    result.setResult(null)
                }
            } catch (ex: Exception) {
                Log.e("CacheViewModel", "Erro!", ex)
                result.setException(ex)
            }
        }
    }.task

    private fun loadDataFromCache(context: Context) {
        context.cacheDir?.also { dir ->
            File(dir, "local_cache").also { file ->
                var cache: CacheData? = null

                if (file.exists()) {
                    FileInputStream(file).runCatching fis@{
                        ObjectInputStream(this).runCatching ois@{
                            cache = readObject() as CacheData
                            this@ois
                        }.getOrNull()?.apply { close() }
                        this@fis
                    }.getOrNull()?.apply { close() }
                }

                if (cache == null) cache = newCache()

                cacheData.value = cache
            }
        }
    }

    private fun saveData(context: Context, data: CacheData) {
        context.cacheDir?.also { dir ->
            File(dir, "local_cache").also { file ->
                if (file.exists()) file.delete()
                FileOutputStream(file).runCatching fos@{
                    ObjectOutputStream(this).runCatching oos@{
                        writeObject(data)
                        this@oos
                    }.getOrNull()?.apply { close() }
                    this@fos
                }.getOrNull()?.apply { close() }
            }
        }
    }

    private fun newCache() = CacheData(System.currentTimeMillis(), arrayOf(), IntArray(0), IntArray(0), IntArray(0), HashMap())

}

