package com.ken.cinema.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ken.cinema.data.repository.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.M)
@HiltViewModel
class SeatViewModel @Inject constructor(
    private val dataStore: DataStore
) : ViewModel() {

    private val _totalSeats = MutableLiveData<Int>()

    val seatIds = dataStore.seatIdsFlow

    fun saveIDDataStore() = viewModelScope.launch {
        dataStore.saveSeatsID(selectedChairIds.value.toString())
    }
    private val selectedChairIds: MutableLiveData<ArrayList<Int>> by lazy {
        MutableLiveData<ArrayList<Int>>().apply {
            value = ArrayList()
        }
    }


    val totalSeats: LiveData<Int>
        get() = _totalSeats

    init {
        _totalSeats.value = 0
    }

    fun addSeatsID(id: Int) {
        if (!selectedChairIds.value?.contains(id)!!) {
            selectedChairIds.value?.add(id)
            _totalSeats.value = (_totalSeats.value)?.plus(1)
        } else {
            selectedChairIds.value?.remove(id)
            _totalSeats.value = (_totalSeats.value)?.minus(1)
        }
    }
}