package com.hackerton.shimton.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.hackerton.shimton.R
import com.hackerton.shimton.data.remote.dto.Room
import com.hackerton.shimton.databinding.ActivityHomeBinding
import com.hackerton.shimton.databinding.DialogEventpageCreateBinding
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

            showCreateDialog()

        }

        //todo room search 추가
        ivHomeSearchImg.setOnSingleClickListener {
            val data = etHomeSearch.text.toString()
            roomViewModel.searchRoomList(data)
        }


    }

    @SuppressLint("SetTextI18n")
    private fun showCreateDialog() {

        val dialogView = Dialog(this)
        val dialogBinding = DialogEventpageCreateBinding.inflate(layoutInflater)
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.setContentView(dialogBinding.root)
        dialogView.show()

        dialogBinding.apply {
            btnDialogCreateRoomComplete.setOnClickListener {
                val name = etDialogCreateRoomNameInput.text.toString()
                val password = etDialogRoomPassword.text.toString()

                if(name.isEmpty()){
                    tvDialogCreateRoomCodeError.text = "이름을 입력해 주세요."
                    tvDialogCreateRoomNameError.isVisible =  true
                    return@setOnClickListener
                }

                if(password.isEmpty()){
                    tvDialogCreateRoomCodeError.text = "비밀번호를 입력해 주세요."
                    tvDialogCreateRoomCodeError.isVisible =  true
                    return@setOnClickListener
                }

                roomViewModel.makeRoom(
                    Room(name, password)
                )
                dialogView.dismiss()

            }
        }
//        val saveButton = dialogView.findViewById<Button>(R.id.saveButton)
//        val allPeriodButton = dialogView.findViewById<Button>(R.id.allPeriodButton)
//        val yearPicker = dialogView.findViewById<NumberPicker>(R.id.yearPicker)
//        val monthPicker = dialogView.findViewById<NumberPicker>(R.id.monthPicker)

    }

}