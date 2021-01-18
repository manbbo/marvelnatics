package br.com.digitalhouse.marvelnaticos.marvelnatics.models.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "comic")
data class ComicDB(
    @PrimaryKey(autoGenerate = false)
    val dbID: Long? = null,

    @ColumnInfo(defaultValue = "0")
    var apiID: Long = 0,

    @ColumnInfo(defaultValue = "")
    var titulo: String,

    @ColumnInfo(defaultValue = "")
    var descricao: String,

    @ColumnInfo(defaultValue = "")
    var dataPublicacao: String,

    @ColumnInfo(defaultValue = "")
    var desenhistas: String,

    @ColumnInfo(defaultValue = "")
    var artistasCapa: String,

    @ColumnInfo(defaultValue = "")
    var imagemCapaUrl: String

)