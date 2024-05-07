package com.example.kpuapps.utils

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.kpuapps.database.Pemilih
import com.example.kpuapps.database.PemilihDao
import com.example.kpuapps.database.PemilihRoom
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// Kelas Repository untuk mengelola data pemilih
class Repository (application: Application) {
    // Dao untuk mengakses data pemilih dari database
    private val mDataPemilihDao: PemilihDao

    // ExecutorService untuk menjalankan operasi database pada thread terpisah
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    // Inisialisasi dao menggunakan database dari PemilihRoom
    init {
        val db = PemilihRoom.getDatabase(application)
        mDataPemilihDao = db.datapemilihDao()
    }

    // Mendapatkan semua data pemilih dari database dalam bentuk LiveData
    fun getAllPemilih(): LiveData<List<Pemilih>> = mDataPemilihDao.getAllPemilih()

    // Menyisipkan data pemilih ke dalam database menggunakan executorService
    fun insert(datapemilih: Pemilih) {
        executorService.execute { mDataPemilihDao.insert(datapemilih) }
    }

    // Mendapatkan data pemilih berdasarkan NIK dalam bentuk LiveData
    fun getPemilihByNIK(nik: String): LiveData<Pemilih> {
        return mDataPemilihDao.getPemilihByNIK(nik)
    }

    // Menghapus data pemilih dari database menggunakan executorService
    fun delete(datapemilih: Pemilih) {
        executorService.execute { mDataPemilihDao.delete(datapemilih) }
    }

    // Mengupdate data pemilih di database menggunakan executorService
    fun update(datapemilih: Pemilih) {
        executorService.execute { mDataPemilihDao.update(datapemilih) }
    }
}
