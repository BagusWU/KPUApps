package com.example.kpuapps.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PemilihDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(pemilih: Pemilih)

    @Update
    fun update(pemilih: Pemilih)

    @Delete
    fun delete(pemilih: Pemilih)

    @Query("SELECT * from pemilih ORDER BY id ASC")
    fun getAllPemilih(): LiveData<List<Pemilih>>

    @Query("SELECT * FROM pemilih WHERE nik = :nik")
    fun getPemilihByNIK(nik: String): LiveData<Pemilih>
}