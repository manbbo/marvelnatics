package br.com.digitalhouse.marvelnaticos.marvelnatics.models.db.wrapper

import androidx.room.Embedded
import androidx.room.Relation
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.db.ComicColecaoInfoDB
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.db.ComicDB

data class ComicWithInfosDB(
    @Embedded
    var comic: ComicDB,

    @Relation(parentColumn = "dbID", entityColumn = "comicID", entity = ComicColecaoInfoDB::class)
    var infos: List<ComicColecaoInfoDB>

)  