package com.hackerton.shimton.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.hackerton.shimton.R
import com.hackerton.shimton.data.remote.dto.Posting
import com.hackerton.shimton.databinding.ActivityMainBinding
import com.hackerton.shimton.view.adapter.PostingAdapter
import com.hackerton.shimton.view.adapter.RoomAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var roomName: String? = null
    private var nickName: String? = null
    private var password: String? = null

    private lateinit var adapter: PostingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        roomName = intent.getStringExtra("roomName")
        nickName = intent.getStringExtra("nickName")
        password = intent.getStringExtra("password")

        //adapter
        adapter = PostingAdapter { posting ->
//            Log.d(TAG, "initViews: ${room.name}")
//            showEnterDialog(room.id, room.name)
        }

        binding.rvPosting.adapter = adapter

        val list = listOf(
            Posting(
                "기해",
                "담배 필 사람 딥",
                "담배.. 한 대 피면서 얘기해용"
            ),
            Posting(
                "로지",
                "춥다..",
                "혹시 담요좀 주실 분"
            ),
            Posting(
                "또가",
                "또가~스",
                "희희.. 냉소바 맛있습니다."
            ),
            Posting(
                "유자",
                "안녕하세요",
                "해커톤 화이팅~"
            ),
            Posting(
                "익호",
                "안드로이드 스터디 하실 분~",
                "제곧내"
            )
        )
        adapter.submitList(list)


    }
}