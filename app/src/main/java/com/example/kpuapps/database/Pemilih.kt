package com.example.kpuapps.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "Pemilih")
@Parcelize
data class Pemilih(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "nik")
    var nik: String? = null,

    @ColumnInfo(name = "nama")
    var nama: String? = null,

    @ColumnInfo(name = "nomorhp")
    var nomorhp: String? = null,

    @ColumnInfo(name = "jeniskelamin")
    var jeniskelamin: String? = null,

    @ColumnInfo(name = "date")
    var date: String? = null,

    @ColumnInfo(name = "alamat")
    var alamat: String? = null,

    @ColumnInfo(name = "latitude")
    var latitude: Double? = null,

    @ColumnInfo(name = "longitude")
    var longitude: Double? = null,

    @ColumnInfo(name = "gambar")
    var gambar: ByteArray? = null

) : Parcelable