package com.hackerton.shimton.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.hackerton.shimton.R
import com.hackerton.shimton.data.remote.dto.Room
import com.hackerton.shimton.databinding.ActivityHomeBinding
import com.hackerton.shimton.databinding.DialogEventpageCreateBinding
import com.hackerton.shimton.databinding.DialogEventpagePasswordBinding
import com.hackerton.shimton.databinding.DialogEventpageProfileBinding
import com.hackerton.shimton.view.adapter.RoomAdapter
import com.hackerton.shimton.view.listener.setOnSingleClickListener
import com.hackerton.shimton.viewmodel.RoomViewModel
import com.ssafy.smartstore.event.EventObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "HomeActivity_sss"

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: RoomAdapter

    private val roomViewModel: RoomViewModel by viewModels()

    private lateinit var nickNameDialogView: Dialog
    private lateinit var nickNamedialogBinding: DialogEventpageProfileBinding
    private var roomId: Long? = null
    private var roomName: String? = null

    private var notiData: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate: ")

        // FCM 토큰 수신
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG + "FCM", "FCM 토큰 얻기에 실패하였습니다.", task.exception)
                return@OnCompleteListener
            }

            // token 전송
//            CoroutineScope(Dispatchers.Main).launch {
//                TokenRepository.INSTANCE.uploadToken(userId, task.result ?: "task.result is null")
//            }
            Log.d(TAG + "FCM", "token: ${task.result ?: "task.result is null"}")

        })

        setFCMChannel()

        //background push 데이터 받기
        notiData = intent?.getStringExtra("notiData")
        Log.d(TAG, "notidata onCreate: $notiData")
        notiData?.let {
//            launch {
//                withContext(Dispatchers.Main) {
//                    notiRepo.insert(Noti(userId, it))
//                }
//                val notiList = notiRepo.select(userId)
//                Log.d(TAG, "onCreate: ${notiList}")
//                homeViewModel.updateNotiList(notiList)
//
//            }
        }

        initViews()
        observeDatas()
    }


    private fun observeDatas() {
        roomViewModel.roomList.observe(this) {
            it?.let {
                Log.d(TAG, "observeDatas: $it")
                adapter.submitList(it.toMutableList())
            }
        }

        roomViewModel.toastMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        roomViewModel.canEnterRoom.observe(this, EventObserver {
            if (it) {
                showNickNameDialog()
            }
        })

        roomViewModel.errorMessage.observe(this){

            if(it.first == "Login"){
                nickNamedialogBinding.tvDialogProfilePasswordError.text = it.second
            }

        }

        roomViewModel.canLogin.observe(this, EventObserver {
            startActivity(Intent(this@HomeActivity, MainActivity::class.java).apply {
                val bundle = Bundle()
                bundle.putParcelable("user", it)
                putExtra("user",bundle)
                finish()
            })
        })

    }


    private fun initViews() = with(binding) {
        //viewmodel
        binding.lifecycleOwner = this@HomeActivity
        binding.vm = roomViewModel

        //adapter
        adapter = RoomAdapter { room ->
            Log.d(TAG, "initViews: ${room.name}")
            showEnterDialog(room.id, room.name)
        }
        rvHome.adapter = adapter

        Log.d(TAG, "initViews: init")
        roomViewModel.getRoomList()


        //todo room api 연동
        ibHomeAddEventpage.setOnSingleClickListener {

            showCreateDialog()
        }

        //todo room search api 연동
        ivHomeSearchImg.setOnSingleClickListener {
            val data = etHomeSearch.text.toString()
            roomViewModel.searchRoomList(data)
        }


    }


    // FCM 노티 채널 설정
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setFCMChannel() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (!it.isSuccessful) {
                Log.d(TAG, "실패: ${it.exception}")
            } else {
                Log.d(TAG, "성공: ${it.result}")
            }
        }
        createNotiChannel("ssafy_id", "ssafy")
    }

    //오레오 이상에서만 동작하는 함수
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotiChannel(channelId: String, channelName: String) {

        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }


    private fun showNickNameDialog() {
        nickNameDialogView = Dialog(this)
        val nickNamedialogBinding = DialogEventpageProfileBinding.inflate(layoutInflater)
        nickNameDialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        nickNameDialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        nickNameDialogView.setContentView(nickNamedialogBinding.root)
        nickNameDialogView.show()
        nickNameDialogView.setCancelable(true)
        nickNamedialogBinding.apply {
            btnEventpageProfileEnterRoom.setOnClickListener {
                val nickName = etDialogProfileNameInput.text.toString()
                val password = etDialogProfilePassword.text.toString()

                if (nickName.isEmpty()) {
                    tvDialogProfileNameError.text = "이름을 입력해 주세요."
                    tvDialogProfileNameError.isVisible = true
                    return@setOnClickListener
                }

                if (password.isEmpty()) {
                    tvDialogProfilePasswordError.text = "비밀번호를 입력해 주세요."
                    tvDialogProfilePasswordError.isVisible = true
                    return@setOnClickListener
                }

                roomId?.let {
                    roomViewModel.login(
                        nickName, password, roomId!!
                    )
                }
                nickNameDialogView.dismiss()
            }
        }
    }


    private fun showEnterDialog(roomId: Long, roomName: String) {
        val dialogView = Dialog(this)
        val dialogBinding = DialogEventpagePasswordBinding.inflate(layoutInflater)
        dialogView.setCancelable(true)
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.setContentView(dialogBinding.root)
        dialogView.show()

        this.roomId = roomId
        this.roomName = roomName

        dialogBinding.apply {
            btnEventpageProfileComplete.setOnClickListener {
                val password = etDialogRoomCodeInput.text.toString()

                if (password.isEmpty()) {
                    tvDialogRoomCodeError.text = "비밀번호를 입력해 주세요."
                    tvDialogRoomCodeError.isVisible = true
                    return@setOnClickListener
                }

                roomViewModel.enterRoom(0, password)
                dialogView.dismiss()

            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun showCreateDialog() {
        val dialogView = Dialog(this)
        val dialogBinding = DialogEventpageCreateBinding.inflate(layoutInflater)
        dialogView.setCancelable(true)
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.setContentView(dialogBinding.root)
        dialogView.show()

        dialogBinding.apply {
            btnDialogCreateRoomComplete.setOnClickListener {
                val name = etDialogCreateRoomNameInput.text.toString()
                val password = etDialogRoomPassword.text.toString()

                if (name.isEmpty()) {
                    tvDialogCreateRoomCodeError.text = "이름을 입력해 주세요."
                    tvDialogCreateRoomNameError.isVisible = true
                    return@setOnClickListener
                }

                if (password.isEmpty()) {
                    tvDialogCreateRoomCodeError.text = "비밀번호를 입력해 주세요."
                    tvDialogCreateRoomCodeError.isVisible = true
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