package com.example.kpuapps.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kpuapps.viewmodel.FormEntryViewModel
import com.example.kpuapps.viewmodel.ListPemilihViewModel

class ViewModelFactory private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        // Variabel INSTANCE yang akan menyimpan satu-satunya instance dari ViewModelFactory
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        // Metode untuk mendapatkan instance dari ViewModelFactory
        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            // Jika INSTANCE null, maka buat instance baru
            if (INSTANCE == null) {
                // Menggunakan synchronized untuk menjaga agar proses pembuatan instance bersifat thread-safe
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application)
                }
            }
            // Mengembalikan INSTANCE yang sudah ada atau yang baru dibuat
            return INSTANCE as ViewModelFactory
        }
    }

    // Fungsi override untuk membuat ViewModel sesuai dengan modelClass yang diberikan
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Membuat ViewModel sesuai dengan modelClass yang diberikan
        return when {
            modelClass.isAssignableFrom(ListPemilihViewModel::class.java) -> {
                ListPemilihViewModel(mApplication) as T
            }
            modelClass.isAssignableFrom(FormEntryViewModel::class.java) -> {
                FormEntryViewModel(mApplication) as T
            }
            // Jika modelClass tidak sesuai dengan yang diharapkan, lemparkan IllegalArgumentException
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
