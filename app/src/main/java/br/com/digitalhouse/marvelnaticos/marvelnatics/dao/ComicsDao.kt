package br.com.digitalhouse.marvelnaticos.marvelnatics.dao


import android.util.Log
import androidx.room.*
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.db.ComicColecaoInfoDB
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.db.ComicDB
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.db.wrapper.ComicWithInfosDB


@Dao
interface ComicsDao {

    //Insert
    @Insert
    suspend fun insertComic(comic: ComicDB): Long

    @Insert
    suspend fun insertComicClassification(comic: ComicColecaoInfoDB): Long

    //Get
    @Transaction
    @Query("SELECT * FROM comic")
    suspend fun getAllComics(): List<ComicDB?>

    @Transaction
    @Query("SELECT info FROM comic_colecao_info WHERE comicID = :comicID")
    suspend fun getAllClassificationsFromComic(comicID: Long): List<String>

    @Transaction
    @Query("SELECT COUNT(*) FROM comic_colecao_info WHERE info = :info")
    suspend fun getComicInfo(info: String): Int

    @Transaction
    @Query("SELECT * FROM comic")
    suspend fun getAllComicsWithAllClassifications(): List<ComicWithInfosDB>

    @Transaction
    @Query("SELECT * FROM comic WHERE apiID = :apiID")
    suspend fun getComicByApiIdWithAllClassifications(apiID: Int): List<ComicWithInfosDB>

    @Transaction
    @Query("SELECT * FROM comic WHERE apiID = :apiID")
    suspend fun getComicById(apiID: Int): List<ComicDB>

    @Transaction
    @Query("SELECT * FROM comic_colecao_info WHERE comicID = :comicID")
    suspend fun getCollectionsById(comicID: Long): List<ComicColecaoInfoDB>

    //Delete
    @Query("DELETE FROM comic_colecao_info WHERE dbid = :dbid AND info = :info")
    suspend fun delByIdAndClassification(dbid: Long, info: String)

    @Query("DELETE FROM comic WHERE dbID = :dbID")
    suspend fun delComicById(dbID: Long)

    @Query("DELETE FROM comic")
    suspend fun deleteAllComics()

    @Query("DELETE FROM comic_colecao_info")
    suspend fun deleteAllInfos()

    suspend fun clearDatabase() {
        deleteAllComics()
        deleteAllInfos()
    }

    suspend fun insertComicDBList(comic: ComicDB, info: String) {
        var listId = getComicById(comic.apiID)
        if (listId.isEmpty()) {
            val id: Long = insertComic(comic)
            insertComicClassification(ComicColecaoInfoDB(null, id, info))
        } else {
            val id = listId[0].dbID!!
            insertComicClassification(ComicColecaoInfoDB(null, id, info))
        }
    }

    suspend fun deleteComicList(comic: ComicDB, info: String) {
        var listComicDB = getComicById(comic.apiID)
        var listComics = getCollectionsById(listComicDB[0].dbID!!)
        var collectionComic: ComicColecaoInfoDB? = null

        for (it in listComics) {
            if (it.info == info) collectionComic = it
        }

        if (collectionComic != null) {
            delByIdAndClassification(collectionComic.dbid ?: 0, collectionComic.info)
            if (listComics.size == 1) {
                delComicById(collectionComic.comicID)
            }
        }
    }
}

