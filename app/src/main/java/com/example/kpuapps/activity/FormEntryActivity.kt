package com.example.kpuapps.activity

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.kpuapps.R
import com.example.kpuapps.database.Pemilih
import com.example.kpuapps.databinding.ActivityFormEntryBinding
import com.example.kpuapps.utils.ViewModelFactory
import com.example.kpuapps.utils.rotateBitmap
import com.example.kpuapps.utils.uriToFile
import com.example.kpuapps.viewmodel.FormEntryViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Calendar


class FormEntryActivity : AppCompatActivity() {

    private var _binding: ActivityFormEntryBinding? = null
    private val binding get() = _binding

    private lateinit var formEntryViewModel: FormEntryViewModel

    private lateinit var selectedDate: String
    private var isEdit = false
    private var pemilih: Pemilih? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFormEntryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        formEntryViewModel = obtainViewModel(this@FormEntryActivity)

        pemilih = intent.getParcelableExtra(EXTRA_NOTE)
        if (pemilih != null) {
            isEdit = true
        } else {
            pemilih = Pemilih()
        }

        selectedDate = binding?.etTanggal?.text.toString()

        binding?.btnCamera?.setOnClickListener { startCameraX() }
        binding?.btnGalery?.setOnClickListener { startGallery() }

        val actionBarTitle: String
        val btnTitle: String
        if (isEdit) {
            actionBarTitle = "Ubah"
            btnTitle = "Update"
            if (pemilih != null) {
                pemilih?.let { pemilih ->
                    binding?.etNik?.setText(pemilih.nik)
                    binding?.etNama?.setText(pemilih.nama)
                    binding?.etNoHp?.setText(pemilih.nomorhp)
                    // Mengisi radio button jenis kelamin
                    when (pemilih.jeniskelamin) {
                        "Laki-Laki" -> binding?.rbLakiLaki?.isChecked = true
                        "Perempuan" -> binding?.rbPerempuan?.isChecked = true
                        else -> {
                        }
                    }
                    binding?.etTanggal?.setText(pemilih.date)
                    binding?.eetAlamat?.setText(pemilih.alamat)
                    binding?.etLatitude?.setText(pemilih.latitude.toString())
                    binding?.etLongitude?.setText(pemilih.longitude.toString())
                    if (pemilih?.gambar != null) {
                        val bitmap = BitmapFactory.decodeByteArray(pemilih?.gambar, 0, pemilih?.gambar?.size ?: 0)
                        binding?.ivPhoto?.setImageBitmap(bitmap)
                    } else {
                        Glide.with(applicationContext)
                            .load(R.drawable.user_picture)
                            .into(binding?.ivPhoto!!)
                    }
                }
            }
        } else {
            actionBarTitle = "Tambah"
            btnTitle = "Submit"
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding?.btnAddDate?.setOnClickListener {
            showDatePicker()
        }

        binding?.btnLokasi?.setOnClickListener {
            val intent = Intent(this@FormEntryActivity, MapsActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_MAPS) // Menggunakan requestCode yang telah didefinisikan
        }

        binding?.btnKirim?.text = btnTitle

        binding?.btnKirim?.setOnClickListener {
            val nik = binding?.etNik?.text.toString().trim()
            val nama = binding?.etNama?.text.toString().trim()
            val nomorhp = binding?.etNoHp?.text.toString().trim()
            val jeniskelamin = when {
                binding?.rbLakiLaki?.isChecked == true -> "Laki-Laki"
                binding?.rbPerempuan?.isChecked == true -> "Perempuan"
                else -> {
                    Log.e("FormEntry", "Jenis Kelamin tidak valid")
                    ""
                }
            }
            val tanggal = selectedDate
            val alamat = binding?.eetAlamat?.text.toString().trim()
            val latitude = binding?.etLatitude?.text.toString().trim()
            val longitude = binding?.etLongitude?.text.toString().trim()

            formEntryViewModel.getDataPemilihByNIK(nik).observe(this) { existingDataPemilih ->
                if (existingDataPemilih != null && (!isEdit || existingDataPemilih.nik != pemilih?.nik)) {
                    binding?.etNik?.error = "NIK already exists"
                    showToast("NIK already exists")
                } else {

                    binding?.etNik?.error = null

                    if (nik.length != 16) {
                        binding?.etNik?.error = "NIK must be 16 digits"
                    } else {

                        binding?.etNik?.error = null

                        if (nama.isEmpty()) {
                            binding?.etNama?.error = "Field can not be blank"
                        } else {

                            binding?.etNama?.error = null

                            if (nomorhp.isEmpty()) {
                                binding?.etNoHp?.error = "Field can not be blank"
                            } else {

                                binding?.etNoHp?.error = null

                                if (alamat.isEmpty()) {
                                    binding?.eetAlamat?.error = "Field can not be blank"
                                } else {

                                    binding?.eetAlamat?.error = null


                                    pemilih.let { pemilih ->
                                        pemilih?.nik = nik
                                        pemilih?.nama = nama
                                        pemilih?.nomorhp = nomorhp
                                        pemilih?.jeniskelamin = jeniskelamin
                                        pemilih?.date = tanggal
                                        pemilih?.alamat = alamat
                                        pemilih?.latitude = latitude.toDoubleOrNull()
                                        pemilih?.longitude = longitude.toDoubleOrNull()
                                        if (getFile != null) {
                                            val imageByteArray = getFile?.readBytes()
                                            pemilih?.gambar = imageByteArray
                                        }
                                    }

                                    if (isEdit) {
                                        formEntryViewModel.update(pemilih as Pemilih)
                                        showToast("Satu item berhasil diubah")
                                    } else {
                                        formEntryViewModel.insert(pemilih as Pemilih)
                                        showToast("Satu item berhasil ditambahkan")
                                    }
                                    finish()
                                }
                            }
                        }
                    }
                }
            }

        }



    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Fungsi menu
        if (isEdit) {
            menuInflater.inflate(R.menu.form_menu, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Implementasi penanganan item menu
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }


    // Fungsi untuk menampilkan DatePicker
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                selectedDate = "$year/${monthOfYear + 1}/$dayOfMonth"
                binding?.etTanggal?.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    // Fungsi untuk menangani hasil dari Activity lain
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Implementasi penanganan hasil Activity
        if (requestCode == REQUEST_CODE_MAPS && resultCode == RESULT_OK && data != null) {
            val latitude = data.getDoubleExtra("latitude", 0.0)
            val longitude = data.getDoubleExtra("longitude", 0.0)

            // Hanya mengatur nilai latitude dan longitude jika mereka valid (tidak 0.0)
            if (latitude != 0.0 && longitude != 0.0) {
                binding?.etLatitude?.setText(latitude.toString())
                binding?.etLongitude?.setText(longitude.toString())
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String

        if (isDialogClose) {
            dialogTitle = "Batal"
            dialogMessage = "Batalkan perubahan?"
        } else {
            dialogMessage = "Hapus data?"
            dialogTitle = "Hapus Data Pemilih"
        }

        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton("Ya") { _, _ ->
                if (!isDialogClose) {
                    formEntryViewModel.delete(pemilih as Pemilih)
                    showToast("Berhasil menghapus item")
                }
                finish()
            }
            setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            val result = rotateBitmap(BitmapFactory.decodeFile(myFile.path), isBackCamera)

            val bytes = ByteArrayOutputStream()
            result.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(this@FormEntryActivity.contentResolver, result, "Title", null)
            val uri = Uri.parse(path.toString())
            getFile = uriToFile(uri, this@FormEntryActivity)

            binding?.ivPhoto?.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@FormEntryActivity)

            getFile = myFile

            binding?.ivPhoto?.setImageURI(selectedImg)
        }
    }


    private fun obtainViewModel(activity: AppCompatActivity): FormEntryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FormEntryViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private var getFile: File? = null
        const val EXTRA_NOTE = "extra_note"
        const val ALERT_DIALOG_CLOSE = 10
        const val CAMERA_X_RESULT = 200
        const val ALERT_DIALOG_DELETE = 20
        private const val REQUEST_CODE_MAPS = 1001
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}

