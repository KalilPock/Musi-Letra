package com.example.musiletra.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.musiletra.data.database.UsuarioDao
import com.example.musiletra.data.database.MusicaDao


@Database(entities = [Usuario::class, MusicaSalva::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun musicaDao(): MusicaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "musiletra_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}