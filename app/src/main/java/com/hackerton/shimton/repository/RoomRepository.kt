package com.hackerton.shimton.repository

import android.util.Log
import com.hackerton.shimton.data.remote.RetrofitClient
import com.hackerton.shimton.data.remote.dto.Room
import com.hackerton.shimton.data.remote.dto.User
import retrofit2.Response

class RoomRepository {

    companion object{
        val INSTANCE = RoomRepository()
    }

    suspend fun getRoomList(): Response<List<Room>> {
//        Log.d("repository", "getUser: $handle")
//        Log.d(TAG, "SolvedacRepository:  $retro")
        return RetrofitClient.roomService.getRoomList()
    }

    suspend fun searchRoomList(): Response<List<Room>> {
//        Log.d("repository", "getUser: $handle")
//        Log.d(TAG, "SolvedacRepository:  $retro")
        return RetrofitClient.roomService.searchRoomList()
    }

    suspend fun makeRoom(room: Room): Response<Room>{
        return RetrofitClient.roomService.makeRoom(room)
    }

    suspend fun enterRoom(room: Room): Response<Room>{
        return RetrofitClient.roomService.enterRoom(room)
    }

    suspend fun login(roomId: Long, nickName: String, password: String): Response<User> {
        return RetrofitClient.roomService.login(roomId, nickName, password)
    }


}