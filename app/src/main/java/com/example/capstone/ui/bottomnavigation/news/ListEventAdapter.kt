package com.example.capstone.ui.bottomnavigation.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstone.data.response.ArticlesItem
import com.example.capstone.databinding.ItemEventBinding

class ListEventAdapter(private val event: (ArticlesItem) -> Unit) : ListAdapter<ArticlesItem, ListEventAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val eventItem = getItem(position)

        holder.title.text = eventItem.title
        holder.desc.text = eventItem.content
        Glide.with(holder.itemView.context).load(eventItem.urltoimage).into(holder.image)

        holder.itemView.setOnClickListener {
            event(eventItem)
        }
    }

    class MyViewHolder(binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.imgItemPhoto
        val title = binding.tvItemName
        val desc = binding.tvItemDescription
    }

    companion object{
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<ArticlesItem>(){
            override fun areItemsTheSame(
                oldItem: ArticlesItem,
                newItem: ArticlesItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ArticlesItem,
                newItem: ArticlesItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}