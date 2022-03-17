package com.ken.cinema.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ken.cinema.data.db.entity.Ticket
import com.ken.cinema.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun saveTicket(ticket: Ticket) = viewModelScope.launch {
        repository.saveTicket(ticket)
    }
}