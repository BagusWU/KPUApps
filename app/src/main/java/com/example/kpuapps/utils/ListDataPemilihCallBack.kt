package com.example.kpuapps.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.kpuapps.database.Pemilih

// Mendefinisikan kelas
class ListDataPemilihCallBack(
    private val oldDataPemilihList: List<Pemilih>, // Properti untuk menyimpan data lama
    private val newDataPemilihList: List<Pemilih> // Properti untuk menyimpan data baru
) : DiffUtil.Callback() { // Mengimplementasikan kelas DiffUtil.Callback()

    // Mengembalikan ukuran data pemilih lama
    override fun getOldListSize(): Int = oldDataPemilihList.size

    /// Mengembalikan ukuran data pemilih baru
    override fun getNewListSize(): Int = newDataPemilihList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Membandingkan apakah item pada posisi tertentu di kedua list identik
        return oldDataPemilihList[oldItemPosition].id == newDataPemilihList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Membandingkan apakah konten dari item pada posisi tertentu di kedua list identik
        val oldItem = oldDataPemilihList[oldItemPosition]
        val newItem = newDataPemilihList[newItemPosition]
        return oldItem.nik == newItem.nik // Membandingkan NIK
                && oldItem.nama == newItem.nama // Membandingkan nama
                && oldItem.nomorhp == newItem.nomorhp // Membandingkan nomor hp
                && oldItem.jeniskelamin == newItem.jeniskelamin // Membandingkan jenis kelamin
                && oldItem.date == newItem.date // Membandingkan tanggal lahir
                && oldItem.alamat == newItem.alamat // Membandingkan alamat
                && oldItem.latitude == newItem.latitude // Membandingkan latitude
                && oldItem.longitude == newItem.longitude // Membandingkan longitude
                && oldItem.gambar.contentEquals(newItem.gambar) // Membandingkan gambar
    }
}
