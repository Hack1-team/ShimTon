package com.hackerton.shimton.data.remote.api

import com.hackerton.shimton.data.remote.dto.Room
import com.hackerton.shimton.data.remote.dto.User
import com.hackerton.shimton.util.Network.MAKE_ROOM
import retrofit2.Response
import retrofit2.http.*

interface RoomService {

    //행사 리스트 받아오기
    @GET("/")
    suspend fun getRoomList(): Response<List<Room>>


    // 행사 검색
//    @POST("${COMMENT}")
//    suspend fun insertComment(@Body comment: Comment): Response<List<Comment>>
    suspend fun searchRoomList(): Response<List<Room>>

    // 행사 추가
    @POST("/${MAKE_ROOM}")
    suspend fun makeRoom(@Body room: Room): Response<String?>

//    @POST(MAKE_ROOM)
    suspend fun enterRoom(@Body room: Room): Response<Room>

    //로그인
    //todo reqeust값, response값
    suspend fun login(roomId: Long, nickname: String, password: String): Response<User>

}