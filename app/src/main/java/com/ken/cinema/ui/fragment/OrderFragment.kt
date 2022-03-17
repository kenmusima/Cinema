package com.ken.cinema.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ken.cinema.R
import com.ken.cinema.databinding.FragmentOrderBinding
import com.ken.cinema.ui.adapter.OrderAdapter
import com.ken.cinema.ui.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderFragment : Fragment(R.layout.fragment_order) {

    private val viewModel by viewModels<OrderViewModel>()
    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOrderBinding.bind(view)
        binding.listTickets.layoutManager = LinearLayoutManager(requireContext())
        val adapter = OrderAdapter()
        binding.listTickets.adapter = adapter

        lifecycle.coroutineScope.launch {
            viewModel.fetchTickets().collect {
                adapter.submitList(it)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}