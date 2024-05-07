package com.example.kpuapps.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.kpuapps.activity.FormEntryActivity.Companion.CAMERA_X_RESULT
import com.example.kpuapps.databinding.ActivityCameraBinding
import com.example.kpuapps.utils.createFile
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private lateinit var cameraExecutor: ExecutorService
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menginisialisasi binding untuk ActivityCameraBinding
        binding = ActivityCameraBinding.inflate(layoutInflater)
        // Mengatur layout activity dengan rootView dari binding
        setContentView(binding.root)
        // Menginisialisasi cameraExecutor dengan newSingleThreadExecutor
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Menambahkan OnClickListener untuk captureImage button
        binding.btnCapture.setOnClickListener { takePhoto() }
        // Menambahkan OnClickListener untuk switchCamera button
        binding.btnSwitch.setOnClickListener {
            // Mengubah cameraSelector ke front/back camera tergantung pada kondisi saat ini
            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA
            // Memulai kamera setelah switch
            startCamera()
        }
    }

    override fun onResume() {
        super.onResume()
        // Sembunyikan UI sistem seperti status bar
        hideSystemUI()
        // Memulai kamera saat onResume dipanggil
        startCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Menutup cameraExecutor saat activity di-destroy
        cameraExecutor.shutdown()
    }

    private fun takePhoto() {
        // Ambil instance imageCapture, jika null kembalikan
        val imageCapture = imageCapture ?: return
        // Buat file foto baru menggunakan createFile
        val photoFile = createFile(application)
        // Buat outputOptions untuk menyimpan gambar ke file foto
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Ambil gambar menggunakan imageCapture
        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    // Tampilkan pesan error jika gagal mengambil gambar
                    Toast.makeText(this@CameraActivity, "Gagal mengambil gambar.", Toast.LENGTH_SHORT).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    // Jika gambar berhasil disimpan, kirim intent dengan data gambar
                    val intent = Intent().apply {
                        putExtra("picture", photoFile)
                        putExtra("isBackCamera", cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                    }
                    // Set result dan selesaikan activity
                    setResult(CAMERA_X_RESULT, intent)
                    finish()
                }
            }
        )
    }

    private fun startCamera() {
        // Untuk memulai kamera.
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also { it.setSurfaceProvider(binding.viewFinder.surfaceProvider) }
            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Toast.makeText(this@CameraActivity, "Gagal memunculkan kamera.", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun hideSystemUI() {
        // Sembunyikan UI sistem seperti status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        // Sembunyikan action bar jika tersedia
        supportActionBar?.hide()
    }
}
