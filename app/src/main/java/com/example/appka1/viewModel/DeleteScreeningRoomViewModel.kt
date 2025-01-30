package com.example.appka1.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appka1.models.ScreeningRoom
import com.example.appka1.network.WroCinemaApi
import kotlinx.coroutines.launch

class DeleteScreeningRoomViewModel : ViewModel() {

    private val _screeningRooms = MutableLiveData<List<ScreeningRoom>>()
    val screeningRooms: LiveData<List<ScreeningRoom>> = _screeningRooms

    fun fetchScreeningRooms() {
        viewModelScope.launch {
            try {
                val roomList = WroCinemaApi.retrofitService.getAllScreeningRooms()
                _screeningRooms.postValue(roomList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteScreeningRoom(roomName: String) {
        viewModelScope.launch {
            try {
                WroCinemaApi.retrofitService.deleteScreeningRoom(roomName)
                fetchScreeningRooms()  // Refresh list after deletion
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}