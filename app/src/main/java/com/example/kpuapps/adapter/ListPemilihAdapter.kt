package com.example.kpuapps.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kpuapps.activity.FormEntryActivity
import com.example.kpuapps.database.Pemilih
import com.example.kpuapps.databinding.LayoutListPemilihBinding
import com.example.kpuapps.utils.ListDataPemilihCallBack

class ListPemilihAdapter : RecyclerView.Adapter<ListPemilihAdapter.DataPemilihViewHolder>() {
    private var listDataPemilih = mutableListOf<Pemilih>()

    // Mengatur daftar data pemilih yang akan ditampilkan
    fun submitList(dataPemilihList: List<Pemilih>) {
        val diffCallback = ListDataPemilihCallBack(this.listDataPemilih, listDataPemilih)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listDataPemilih.clear()
        this.listDataPemilih.addAll(dataPemilihList.toMutableList())
        diffResult.dispatchUpdatesTo(this)
    }


    // Membuat tampilan untuk setiap item data pemilih
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataPemilihViewHolder {
        val binding = LayoutListPemilihBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataPemilihViewHolder(binding)
    }

    // Mengikat data pemilih ke tampilan item
    override fun onBindViewHolder(holder: DataPemilihViewHolder, position: Int) {
        holder.bind(listDataPemilih[position])
    }

    // Mengembalikan jumlah item dalam daftar data pemilih
    override fun getItemCount(): Int {
        return listDataPemilih.size
    }

    // ViewHolder untuk setiap item data pemilih
    inner class DataPemilihViewHolder(private val binding: LayoutListPemilihBinding) : RecyclerView.ViewHolder(binding.root) {
        // Mengikat data pemilih ke tampilan item
        fun bind(datapemilih: Pemilih) {
            with(binding) {
                tvItemNik.text = datapemilih.nik?.toString() ?: ""
                tvItemDate.text = datapemilih.date
                tvItemNama.text = datapemilih.nama

                // Menambahkan onClickListener untuk membuka FormEntryActivity dengan data pemilih yang dipilih
                cvItemDatapemilih.setOnClickListener {
                    val intent = Intent(it.context, FormEntryActivity::class.java)
                    intent.putExtra(FormEntryActivity.EXTRA_NOTE, datapemilih)
                    it.context.startActivity(intent)
                }
            }
        }
    }
}
