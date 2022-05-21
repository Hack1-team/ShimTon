package com.hackerton.shimton.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.hackerton.shimton.databinding.ActivityHomeBinding
import com.hackerton.shimton.view.adapter.RoomAdapter
import com.hackerton.shimton.view.listener.setOnSingleClickListener
import com.hackerton.shimton.viewmodel.RoomViewModel

private const val TAG = "HomeActivity_sss"

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: RoomAdapter

    private val roomViewModel : RoomViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate: ")
        initViews()
        observeDatas()
    }


    private fun observeDatas(){
        roomViewModel.roomList.observe(this){
            it?.let {
                Log.d(TAG, "observeDatas: $it")
                adapter.submitList(it.toMutableList())
            }
        }

        roomViewModel.toastMessage.observe(this){
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

    }


    private fun initViews() = with(binding){
        //viewmodel
        binding.lifecycleOwner = this@HomeActivity
        binding.vm = roomViewModel

        //adapter
        adapter = RoomAdapter{ room ->
            Log.d(TAG, "initViews: ${room.name}")
        }
        rvHome.adapter = adapter

        Log.d(TAG, "initViews: init")
        roomViewModel.getRoomList()


        //todo room 추가
        ibHomeAddEventpage.setOnSingleClickListener {

        }

        //todo room search 추가
        ivHomeSearchImg.setOnSingleClickListener {
            val data = etHomeSearch.text.toString()
            roomViewModel.searchRoomList(data)
        }


    }

}