package com.hackerton.shimton.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackerton.shimton.data.remote.RetrofitClient
import com.hackerton.shimton.data.remote.dto.Room
import com.hackerton.shimton.repository.RoomRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response

class RoomViewModel : ViewModel() {

    var job: Job? = null

    private var _responseGetRoomList = MutableLiveData<Response<List<Room>>>()
    val responseGetRoomList: LiveData<Response<List<Room>>>
        get() = _responseGetRoomList

    private var _roomList = MutableLiveData<List<Room>>()
    val roomList: LiveData<List<Room>>
        get() = _roomList


    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private var _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    //코루틴 예외처리 핸들러
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    private fun onError(message: String) {
        _errorMessage.value = message
        _loading.value = false

    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }


    fun makeRoom(room: Room) = viewModelScope.launch{
        var response: Response<Room>? = null
        val job = launch(Dispatchers.Main + exceptionHandler) {
            response = RoomRepository.INSTANCE.makeRoom(room)
        }
        job.join()
        response?.let {
            if (it.isSuccessful) {
                it.body()?.let { result ->
                    when (it.code()) {
                        200 -> {
                            //업데디트 완료시 리스트뷰 갱신
                            _toastMessage.postValue("트랙이 생성되었습니다.")
                            getRoomList()
                        }
                        else -> onError(it.message())
                    }
                }
            } else {
                it.errorBody()?.let { errorBody ->
                    RetrofitClient.getErrorResponse(errorBody)?.let {
                        onError(it.message)
                        Log.d("viewmodel", "observerDatas: $it")
                    }
                }
            }
        }
    }

    fun searchRoomList(searchData: String) = viewModelScope.launch {

        if(searchData.isEmpty()){
            _toastMessage.postValue("검색할 값을 입력해 주세요")
            return@launch
        }

        var response: Response<List<Room>>? = null
        val job = launch(Dispatchers.Main + exceptionHandler) {
            response = RoomRepository.INSTANCE.searchRoomList()
        }
        job.join()
        response?.let {
            if (it.isSuccessful) {
                it.body()?.let { result ->
                    when (it.code()) {
                        200 -> {
                            _roomList.postValue(result)
                        }
                        else -> onError(it.message())
                    }
                }
            } else {
                it.errorBody()?.let { errorBody ->
                    RetrofitClient.getErrorResponse(errorBody)?.let {
                        onError(it.message)
                        Log.d("viewmodel", "observerDatas: $it")
                    }
                }
            }
        }

    }


    fun getRoomList() = viewModelScope.launch {

        val list = listOf<Room>(
            Room("너디너리 해커톤1","1234"),
            Room("쉼톤 해커톤2","1234"),
            Room("cmc 해커톤3","1234"),
            Room("umc 해커톤4","1234"),
            Room("메이커스 해커톤5","1234"),
            Room("프론트원 해커톤6","1234"),
            Room("너디너리 해커톤7","1234")
        )

        _roomList.postValue(list)

//        var response: Response<List<Room>>? = null
//        val job = launch(Dispatchers.Main + exceptionHandler) {
//            response = RoomRepository.INSTANCE.getRoomList()
//        }
//        job.join()
//        response?.let {
//            if (it.isSuccessful) {
//                it.body()?.let { result ->
//                    //todo code에 따른 분기처리하여 메시지
//                    when(it.code()){
//                        200 ->{
//                            _roomList.postValue(result)
//                        }
//                        else -> onError(it.message())
//                    }
//                }
//            } else {
//                it.errorBody()?.let { errorBody ->
//                    RetrofitClient.getErrorResponse(errorBody)?.let {
//                        onError(it.message)
//                        Log.d("viewmodel", "observerDatas: $it")
//                    }
//                }
//            }
//        }
    }


}