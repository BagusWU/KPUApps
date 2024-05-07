package com.example.kpuapps.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.example.kpuapps.adapter.ListPemilihAdapter
import com.example.kpuapps.databinding.ActivityListPemilihBinding
import com.example.kpuapps.utils.ViewModelFactory
import com.example.kpuapps.viewmodel.ListPemilihViewModel

class ListPemilihActivity : AppCompatActivity() {

    private var _binding: ActivityListPemilihBinding? = null
    private val binding get() = _binding

    private lateinit var adapter: ListPemilihAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityListPemilihBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        supportActionBar?.title = "Daftar Data Pemilih"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val daftarDataPemilihViewModel = obtainViewModel(this@ListPemilihActivity)


        daftarDataPemilihViewModel.getAllDataPemilih().observe(this) { datapemilihList ->
            if (datapemilihList != null && datapemilihList.isNotEmpty()) {
                adapter.submitList(datapemilihList)
            } else {
                adapter.submitList(emptyList()) // Menetapkan daftar kosong untuk menghapus data sebelumnya.
                showNoDataSnackbar()
            }
        }

        adapter = ListPemilihAdapter()


        binding?.rvDatapemilih?.layoutManager = LinearLayoutManager(this)
        binding?.rvDatapemilih?.setHasFixedSize(true)
        binding?.rvDatapemilih?.adapter = adapter

    }


    private fun showNoDataSnackbar() {
        val snackbar = Snackbar.make(
            binding?.root!!, // Root view dari layout
            "Tidak ada data saat ini",
            Snackbar.LENGTH_LONG
        )
        snackbar.show()
    }


    private fun obtainViewModel(activity: AppCompatActivity): ListPemilihViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(ListPemilihViewModel::class.java)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
