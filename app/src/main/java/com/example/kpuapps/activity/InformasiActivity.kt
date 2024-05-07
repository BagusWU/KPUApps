package com.example.kpuapps.activity

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kpuapps.R
import com.example.kpuapps.databinding.ActivityInformasiBinding

class InformasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInformasiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengatur tampilan tombol kembali di action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding.webView.settings.javaScriptEnabled = true

        // Menangani event selesai memuat halaman web.
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                // Menjalankan JavaScript untuk menampilkan alert pada halaman web.
                view.loadUrl("javascript:alert('Web KPU berhasil dimuat')")
            }
        }

        // Menangani event alert JavaScript di dalam WebView.
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView, url: String, message: String, result: android.webkit.JsResult): Boolean {
                // Menampilkan pesan alert dengan Toast.
                Toast.makeText(this@InformasiActivity, message, Toast.LENGTH_LONG).show()
                result.confirm() // Mengkonfirmasi penanganan alert.
                return true
            }
        }

        // Untuk menampilkan laman dari link URL
        binding.webView.loadUrl(getString(R.string.link_kpu_web))

    }

    override fun onSupportNavigateUp(): Boolean {
        // Fungsi untuk kembali di action bar
        onBackPressed()
        return true
    }
}