package com.example.kpuapps.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Pemilih::class], version = 1)
abstract class PemilihRoom : RoomDatabase() {

    // Mendefinisikan fungsi abstrak untuk mendapatkan DAO Pemilih
    abstract fun datapemilihDao(): PemilihDao

    // Companion object untuk menyimpan instance database
    companion object {
        // Instance database yang bersifat volatile agar nilainya dapat dilihat dan diperbarui oleh semua thread
        @Volatile
        private var INSTANCE: PemilihRoom? = null

        // Fungsi untuk mendapatkan instance database
        @JvmStatic
        fun getDatabase(context: Context): PemilihRoom {
            // Menggunakan double-checked locking untuk memastikan hanya satu instance yang dibuat
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        // Fungsi untuk membuat instance baru database menggunakan Room
        private fun buildDatabase(context: Context): PemilihRoom {
            return Room.databaseBuilder(context.applicationContext, PemilihRoom::class.java, "datapemilih_database")
                .build()
        }
    }
}

