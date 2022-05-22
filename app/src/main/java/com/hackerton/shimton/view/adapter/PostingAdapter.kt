package com.hackerton.shimton.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hackerton.shimton.data.remote.dto.Posting
import com.hackerton.shimton.data.remote.dto.Room
import com.hackerton.shimton.databinding.ItemEventpageArticleBinding
import com.hackerton.shimton.databinding.ItemEventpageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostingAdapter(val onPostingClicked: (Posting) -> Unit ) :
    ListAdapter<Posting, PostingAdapter.ItemViewHolder>(diffUtil) {

    inner class ItemViewHolder(private val binding: ItemEventpageArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(posting: Posting) {
            binding.posting = posting
            binding.root.setOnClickListener {
                onPostingClicked(posting)
            }
//            binding.executePendingBindings()//데이터가 수정되면 즉각 바인딩
//            binding.menus = menus
//            val url = menus.images!![2].url
//
//            CoroutineScope(Dispatchers.Main).launch {
//
//                val bitmap = withContext(Dispatchers.IO) {
//                    ImageConverter.convertImage(url)
//                }
//                binding.itemMenuIv.setImageBitmap(bitmap)
//                binding.itemMenuIv.clipToOutline = true
//            }
//
//            binding.root.setOnClickListener {
//                itemClick(menus)
//            }
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
        Log.d("adapter", "onBindViewHolder: ${position}")
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            ItemEventpageArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Posting>() {
            override fun areItemsTheSame(oldItem: Posting, newItem: Posting): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Posting, newItem: Posting): Boolean {
                return oldItem == newItem
            }

        }
    }

}