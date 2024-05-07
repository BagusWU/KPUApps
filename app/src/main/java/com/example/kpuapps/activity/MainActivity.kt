package com.example.kpuapps.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kpuapps.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Pindah ke halaman informasi
        binding.btnInformasi.setOnClickListener {
            val intent = Intent(this@MainActivity, InformasiActivity::class.java)
            startActivity(intent)

        }

        // Pindah ke halaman form entry
        binding.btnForm.setOnClickListener {
            val intent = Intent(this@MainActivity, FormEntryActivity::class.java)
            startActivity(intent)

        }

        // Pindah ke halaman list (Lihat Data)
        binding.btnListInfo.setOnClickListener {
            val intent = Intent(this@MainActivity, ListPemilihActivity::class.java)
            startActivity(intent)

        }

        binding.btnOut.setOnClickListener {
            finish()
        }
    }
}