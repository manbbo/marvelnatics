package br.com.digitalhouse.marvelnaticos.marvelnatics.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.cache.CacheData
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.cache.ComicCache
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.firebase.ComicFB
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.firebase.ComicGlobalInfoFB
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.firebase.UserDataFB
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main.OfflineViewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.thread

class FirebaseViewModel : ViewModel() {

    @Volatile
    private var currentUserToken: String? = null

    val isUserAvaliable = MutableLiveData<Boolean>(false)


    fun loadMainDataForCache() = TaskCompletionSource<CacheData>().also { result ->
        if (currentUserToken == null) result.setException(Exception("Acesso indisponível"))
        currentUserToken?.also { userToken ->
            thread {
                try {
                    val destaque = Tasks.await(firestoreComicInfo().orderBy("totals.Q", Query.Direction.DESCENDING).limit(10).get()).toObjects(ComicGlobalInfoFB::class.java).toTypedArray().let { data ->
                        IntArray(data.size) { i -> data[i].comicID }
                    }

                    val lidas = Tasks.await(firestoreComicInfo().orderBy("totals.J", Query.Direction.DESCENDING).limit(10).get()).toObjects(ComicGlobalInfoFB::class.java).toTypedArray().let { data ->
                        IntArray(data.size) { i -> data[i].comicID }
                    }

                    val avaliadas = Tasks.await(firestoreComicInfo().whereGreaterThan("ratingAverage", 0).orderBy("ratingAverage", Query.Direction.DESCENDING).limit(10).get()).toObjects(ComicGlobalInfoFB::class.java).toTypedArray().let { data ->
                        IntArray(data.size) { i -> data[i].comicID }
                    }

                    val comicIDs = mutableSetOf<Int>().also { set ->
                        set.addAll(destaque.asIterable())
                        set.addAll(lidas.asIterable())
                        set.addAll(avaliadas.asIterable())
                    }.toIntArray()

                    val userAvaliadas = Tasks.await(firestoreComicList().whereGreaterThanOrEqualTo("rating.$userToken", 0).get()).toObjects(ComicFB::class.java).toTypedArray().let { data ->
                        HashMap<Int, Int>().also { map ->
                            data.forEach { c ->
                                c.rating[userToken]?.also { r -> map[c.apiID] = r }
                            }
                        }
                    }


                    val tasks = mutableListOf<Task<DocumentSnapshot>>()

                    val comics = Collections.synchronizedList(mutableListOf<ComicFB>())


                    val lock = Object()

                    comicIDs.onEach { comicID ->
                        tasks.add(getComicUsingTask(comicID).also { task ->
                            task.addOnSuccessListener { doc ->
                                doc.toObject(ComicFB::class.java)?.let {
                                    synchronized(comics) {
                                        comics.add(it)
                                    }
                                    synchronized(lock) {
                                        lock.notifyAll()
                                    }
                                }
                            }
                        })
                    }

                    Tasks.await(Tasks.whenAll(tasks))
                    while (synchronized(comics) { comics.size } < tasks.size) {
                        synchronized(lock) {
                            lock.wait()
                        }
                    }
                    result.setResult(CacheData(System.currentTimeMillis(), synchronized(comics) { Array(comics.size) { i -> ComicCache.from(comics[i]) } }, destaque, lidas, avaliadas, userAvaliadas))
                } catch (ex: Exception) {
                    Log.e("FirebaseViewModel", "Erro!", ex)
                    result.setException(Exception("Não foi possível carregar as informações"))
                }
            }
        }
    }.task

    fun getComicRatingAverage(comic: Int) = TaskCompletionSource<Double>().also { result ->
        thread {
            try {
                result.setResult(getComicInfo(comic).ratingAverage)
            } catch (ex: Exception) {
                Log.e("FirebaseViewModel", "Erro!", ex)
                result.setException(Exception("Não foi possível consultar as informações"))
            }
        }
    }.task

    fun submitComicRating(comic: Int, rating: Int): Task<Nothing> {
        val result = TaskCompletionSource<Nothing>()
        if (currentUserToken == null) result.setException(Exception("Acesso indisponível"))
        currentUserToken?.also { userToken ->
            thread {
                val comicData = getComic(comic)
                val comicInfo = getComicInfo(comic)

                val hasRatedBefore = comicData?.rating?.containsKey(userToken) ?: false

                val lastRate = comicData?.rating?.get(userToken) ?: 0


                comicInfo.ratingUsers += if (hasRatedBefore) 0 else 1
                comicInfo.ratingTotal += rating - lastRate
                comicInfo.ratingAverage = comicInfo.ratingTotal.toDouble() / comicInfo.ratingUsers

                try {
                    Tasks.await(firestoreComicList().document("$comic").update("rating.$userToken", rating))
//                    Tasks.await(firestoreComicList().document("$comic").collection("rating").add(userToken to rating))
                    Tasks.await(firestoreComicInfo().document("$comic").set(comicInfo, SetOptions.merge()))
                    result.setResult(null)
                } catch (ex: Exception) {
                    Log.e("FirebaseViewModel", "Erro!", ex)
                    result.setException(Exception("Não foi possível salvar as alterações"))
                }
            }
        }
        return result.task
    }

    fun changeComicInList(comic: Int, list: String, status: Boolean) = TaskCompletionSource<Nothing>().also { result ->
        if (currentUserToken == null) result.setException(Exception("Acesso indisponível"))
        currentUserToken?.also { userToken ->
            thread {
                try {
                    val userData = getUserData(userToken)
                    val comicInfo = getComicInfo(comic)

                    val changed = if (status) userData.addComicInList(comic, list) else userData.remComicFromList(comic, list)

                    if (changed) {
                        comicInfo.totals[list] = (comicInfo.totals[list] ?: 0) + (if (status) 1 else -1)

                        Tasks.await(firestoreUserData().document(userToken).set(userData, SetOptions.merge()))

                        Tasks.await(firestoreComicInfo().document("$comic").set(comicInfo, SetOptions.merge()))
                        result.setResult(null)

                    } else result.setResult(null)
                } catch (ex: Exception) {
                    Log.e("FirebaseViewModel", "Erro!", ex)
                    result.setException(Exception("Não foi possível alterar as listas"))
                }
            }
        }
    }.task


    fun uploadComic(comic: ComicFB) = TaskCompletionSource<Nothing>().also { result ->
        try {
            Tasks.await(firestoreComicList().document("${comic.apiID}").set(comic, SetOptions.merge()))
            result.setResult(null)
        } catch (ex: Exception) {
            Log.e("FirebaseViewModel", "Erro!", ex)
            result.setException(Exception("Não foi possível salvar a comic"))
        }
    }.task

    private fun getComicInfo(comic: Int) = Tasks.await(firestoreComicInfo().document("$comic").get()).toObject(ComicGlobalInfoFB::class.java) ?: ComicGlobalInfoFB(comic, 0.0, 0, 0, mutableMapOf())

    private fun getComic(comic: Int) = Tasks.await(getComicUsingTask(comic)).toObject(ComicFB::class.java)

    private fun getComicUsingTask(comic: Int) = firestoreComicList().document("$comic").get()

    private fun getUserData(userToken: String) = Tasks.await(firestoreUserData().document(userToken).get()).toObject(UserDataFB::class.java) ?: UserDataFB(userToken, mutableMapOf())


    private fun firestore() = FirebaseFirestore.getInstance()
    private fun firestoreComicList() = firestore().collection("comics")
    private fun firestoreUserData() = firestore().collection("users")
    private fun firestoreComicInfo() = firestore().collection("comic_info")


    fun loadUserDataToLocal(offlineViewModel: OfflineViewModel) = TaskCompletionSource<Nothing>().also { result ->
        if (currentUserToken == null) result.setException(Exception("Acesso indisponível"))
        currentUserToken?.also { userToken ->
            thread {
                try {
                    val userData = getUserData(userToken)
                    Tasks.await(offlineViewModel.clearDatabase())

                    val comicIDs = mutableSetOf<Int>().also { set ->
                        userData.comicInfos.values.forEach { comics -> set.addAll(comics) }
                    }.toIntArray()

                    val comics = loadAllComics(comicIDs)


                    userData.comicInfos.entries.forEach { entry ->
                        entry.value.forEach { comicID ->
                            comics.find { c -> c.apiID == comicID }?.also { comic ->
                                Tasks.await(offlineViewModel.insertComicInList(comic.apiID, comic.title, comic.description, comic.releaseDate, comic.artistDrawer, comic.artistCover, comic.artistCreator, comic.coverUrl, entry.key))
                            }
                        }
                    }
                    result.setResult(null)
                } catch (ex: Exception) {
                    Log.e("FirebaseViewModel", "Erro!", ex)
                    result.setException(Exception("Não foi carregar as informações"))
                }
            }
        }
    }.task


    private fun loadAllComics(comicIDs: IntArray): Array<ComicFB> {
        val tasks = mutableListOf<Task<DocumentSnapshot>>()
        val comics = Collections.synchronizedList(mutableListOf<ComicFB>())

        val lock = Object()

        comicIDs.onEach { comicID ->
            tasks.add(getComicUsingTask(comicID).also { task ->
                task.addOnSuccessListener { doc ->
                    doc.toObject(ComicFB::class.java)?.let {
                        synchronized(comics) {
                            comics.add(it)
                        }
                        synchronized(lock) {
                            lock.notifyAll()
                        }
                    }
                }
            })
        }

        Tasks.await(Tasks.whenAll(tasks))
        while (synchronized(comics) { comics.size } < tasks.size) {
            synchronized(lock) {
                lock.wait()
            }
        }
        return comics.toTypedArray()
    }


    fun setup() {
        viewModelScope.launch {
            currentUserToken = Firebase.auth.currentUser?.uid
            isUserAvaliable.value = currentUserToken != null
        }
    }


}