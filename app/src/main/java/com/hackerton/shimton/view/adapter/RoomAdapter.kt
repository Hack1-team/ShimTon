package com.hackerton.shimton.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hackerton.shimton.data.remote.dto.Room
import com.hackerton.shimton.databinding.ItemEventpageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomAdapter( val onRoomClicked: (Room) -> Unit ) :
    ListAdapter<Room, RoomAdapter.ItemViewHolder>(diffUtil) {

    inner class ItemViewHolder(private val binding: ItemEventpageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(room: Room) {
            binding.room = room
            binding.root.setOnClickListener {
                onRoomClicked(room)
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
            ItemEventpageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Room>() {
            override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }

}