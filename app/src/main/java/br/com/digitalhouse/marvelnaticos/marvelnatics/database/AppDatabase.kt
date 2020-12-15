package br.com.digitalhouse.marvelnaticos.marvelnatics.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.digitalhouse.marvelnaticos.marvelnatics.dao.ComicsDao
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.db.ComicColecaoInfoDB
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.db.ComicDB


@Database(entities = [ComicDB::class, ComicColecaoInfoDB::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun comicsDao(): ComicsDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        /*
            CHAMAR DENTRO DE UMA ACTIVITY

            fun initBD(){
                db = AppDatabase.invoke(this)
            }

         */
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "marvelnatics.db"
        ).build()
    }

}