package br.com.digitalhouse.marvelnaticos.marvelnatics.models.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comic_colecao_info")
data class ComicColecaoInfoDB(
    @PrimaryKey(autoGenerate = true)
    val dbid: Long? = null,

    @ColumnInfo(defaultValue = "0")
    var comicID: Long = 0,

    @ColumnInfo(defaultValue = "")
    var info: String = ""
)