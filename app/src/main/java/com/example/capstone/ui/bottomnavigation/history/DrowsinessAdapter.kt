package com.example.capstone.ui.bottomnavigation.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstone.data.room.DrowsinessRecord
import com.example.capstone.databinding.ItemDrowsinessBinding

class DrowsinessAdapter(private val records: List<DrowsinessRecord>) : RecyclerView.Adapter<DrowsinessAdapter.DrowsinessViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrowsinessViewHolder {
        val binding = ItemDrowsinessBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DrowsinessViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DrowsinessViewHolder, position: Int) {
        val record = records[position]
        holder.bind(record)
    }

    override fun getItemCount(): Int = records.size

    class DrowsinessViewHolder(private val binding: ItemDrowsinessBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: DrowsinessRecord) {
            binding.timestampText.text = record.timestamp
            binding.messageText.text = record.message
            
            Glide.with(binding.imageView.context)
                .load(record.image)
                .into(binding.imageView)
        }
    }
}
