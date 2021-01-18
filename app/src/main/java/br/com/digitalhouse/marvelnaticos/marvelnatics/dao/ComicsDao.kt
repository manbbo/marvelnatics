package br.com.digitalhouse.marvelnaticos.marvelnatics.dao


import androidx.room.*
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.db.ComicColecaoInfoDB
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.db.ComicDB


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
    suspend fun getAllComics(): List<ComicDB>

    @Transaction
    @Query("SELECT info FROM comic_colecao_info WHERE comicID = :comicID")
    suspend fun getAllClassificationFromComic(comicID: Long): List<String>

    @Transaction
    @Query("SELECT * FROM comic INNER JOIN comic_colecao_info ON comic.dbID = comic_colecao_info.comicID AND comic_colecao_info.info = :info")
    suspend fun getAllComicsFromClassification(info: String): List<ComicDB>

    @Transaction
    @Query("SELECT * FROM comic WHERE apiID = :apiID")
    suspend fun getComicById(apiID: Long): List<ComicDB>

    @Transaction
    @Query("SELECT * FROM comic_colecao_info WHERE comicID = :comicID")
    suspend fun getColectionByIdAndClassification(comicID: Long): List<ComicColecaoInfoDB>



    //Delete
    @Query("DELETE FROM comic_colecao_info WHERE dbid = :dbid")
    suspend fun delByIdAncClassification(dbid: Long)

    //Delete
    @Query("DELETE FROM comic WHERE dbID = :dbID")
    suspend fun delComicById(dbID: Long)


    suspend fun insertComicList(comic: ComicDB, info: String) {
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
        var comic = getComicById(comic.apiID)
        var listComics = getColectionByIdAndClassification(comic[0].dbID!!)
        lateinit var colectionComic: ComicColecaoInfoDB
        listComics.forEach { if (it.info == info) colectionComic = it }
        delByIdAncClassification(colectionComic.dbid!!)
        if (listComics.size == 1) {
            delComicById(colectionComic.comicID)
        }
    }




}

