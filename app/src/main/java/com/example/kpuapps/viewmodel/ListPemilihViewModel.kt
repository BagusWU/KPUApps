package com.example.kpuapps.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.kpuapps.database.Pemilih
import com.example.kpuapps.utils.Repository

// Kelas ListPemilihViewModel adalah turunan dari kelas ViewModel.
class ListPemilihViewModel(application: Application) : ViewModel() {

    // Inisialisasi variabel mDataPemilihRepository dengan objek Repository yang diinisialisasi dengan parameter application.
    private val mDataPemilihRepository: Repository = Repository(application)

    // Fungsi getAllDataPemilih() mengembalikan objek LiveData yang berisi daftar pemilih dari Repository.
    // LiveData digunakan untuk mengamati perubahan data secara asinkron.
    fun getAllDataPemilih(): LiveData<List<Pemilih>> = mDataPemilihRepository.getAllPemilih()
}
