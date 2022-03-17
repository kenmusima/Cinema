package com.ken.cinema.ui.viewmodel

import android.os.Build
import android.os.Bundle
import android.util.ArraySet
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.datastore.dataStore
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner
import com.ken.cinema.data.repository.DataStore
import com.ken.cinema.util.SEATS_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.M)
@HiltViewModel
class SeatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dataStore: DataStore
) : ViewModel() {

    private val _totalSeats = MutableLiveData<Int>()

    val seatIds = dataStore.seatIdsFlow

    fun saveIDDataStore(seatIDs: String) = viewModelScope.launch {
        dataStore.saveSeatsID(seatIDs)
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

    fun saveSeatIds() {
        savedStateHandle.set(SEATS_ID, selectedChairIds.value?.toTypedArray())
    }

    fun getSeatIds(): LiveData<Array<Int>> {
        return savedStateHandle.getLiveData(SEATS_ID)
    }
}