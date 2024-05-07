package com.example.kpuapps.utils

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import com.example.kpuapps.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale


// Format untuk nama file
private const val FILENAME_FORMAT = "dd-MMM-yyyy"

// Timestamp saat ini diformat sesuai FILENAME_FORMAT
val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())

fun createTempFile(context: Context): File {
    // Dapatkan direktori untuk menyimpan gambar
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir) // Buat file sementara dengan timestamp sebagai nama dan ekstensi .jpg
}

fun createFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let { // Dapatkan direktori media eksternal pertama
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() } // Buat direktori dengan nama aplikasi di direktori media
    }
    val outputDirectory = if (mediaDir != null && mediaDir.exists()) mediaDir else application.filesDir // Set direktori output sebagai direktori media yang dibuat jika ada, jika tidak gunakan direktori file aplikasi
    return File(outputDirectory, "$timeStamp.jpg") // Kembalikan file dengan timestamp sebagai nama dan ekstensi .jpg di direktori output
}

fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
    val matrix = Matrix() // Buat matriks baru
    return if (isBackCamera) { // Jika gambar berasal dari kamera belakang
        matrix.postRotate(90f) // Putar matriks sebesar 90 derajat
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true) // Buat bitmap baru dengan matriks yang diputar
    } else { // Jika gambar bukan berasal dari kamera belakang
        matrix.postRotate(-90f) // Putar matriks sebesar -90 derajat
        matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f) // Skala matriks
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true) // Buat bitmap baru dengan matriks yang diputar dan diskalakan
    }
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver // Dapatkan resolver konten
    val myFile = createTempFile(context) // Buat file sementara
    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream // Buka input stream ke gambar yang dipilih
    val outputStream: OutputStream = FileOutputStream(myFile) // Buat output stream ke file sementara
    val buf = ByteArray(1024) // Buat buffer
    var len: Int // Inisialisasi variabel untuk panjang data yang dibaca
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len) // Baca data dari input stream dan tulis ke output stream
    outputStream.close() // Tutup output stream
    inputStream.close() // Tutup input stream
    return myFile // Kembalikan file sementara
}
