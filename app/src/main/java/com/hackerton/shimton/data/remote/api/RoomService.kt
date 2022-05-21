package com.hackerton.shimton.data.remote.api

import com.hackerton.shimton.data.remote.dto.Room
import retrofit2.Response
import retrofit2.http.*

interface RoomService {

    //행사 리스트 받아오기
//    @GET("${COMMENT}/{id}")
    suspend fun getRoomList(): Response<List<Room>>


    // 행사 검색
//    @POST("${COMMENT}")
//    suspend fun insertComment(@Body comment: Comment): Response<List<Comment>>
    suspend fun searchRoomList(): Response<List<Room>>

    // 행사 추가
    suspend fun makeRoom(room: Room): Response<Room>
}