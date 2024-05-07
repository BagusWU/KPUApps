package com.example.kpuapps.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.kpuapps.database.Pemilih
import com.example.kpuapps.utils.Repository

class FormEntryViewModel (application: Application) : AndroidViewModel(application) {

    // Mendeklarasikan repository untuk mengakses data pemilih
    private val mDataPemilihRepository: Repository = Repository(application)

    // Fungsi untuk menyimpan data pemilih ke repository
    fun insert(datapemilih: Pemilih) {
        mDataPemilihRepository.insert(datapemilih)
    }

    // Fungsi untuk mengupdate data pemilih di repository
    fun update(datapemilih: Pemilih) {
        mDataPemilihRepository.update(datapemilih)
    }

    // Fungsi untuk mendapatkan data pemilih berdasarkan NIK dari repository
    fun getDataPemilihByNIK(nik: String): LiveData<Pemilih> {
        return mDataPemilihRepository.getPemilihByNIK(nik)
    }

    // Fungsi untuk menghapus data pemilih dari repository
    fun delete(datapemilih: Pemilih) {
        mDataPemilihRepository.delete(datapemilih)
    }

}