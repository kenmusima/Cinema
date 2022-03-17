package com.ken.cinema.ui.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ken.cinema.R
import com.ken.cinema.data.model.Film
import com.ken.cinema.databinding.FragmentSeatBinding
import com.ken.cinema.ui.viewmodel.SeatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.M)
@AndroidEntryPoint
class SeatFragment : Fragment(R.layout.fragment_seat) {

    private val args: SeatFragmentArgs by navArgs()
    private var _binding: FragmentSeatBinding? = null
    private val binding get() = _binding!!

    private lateinit var film: Film
    private val viewModel by viewModels<SeatViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSeatBinding.bind(view)
        setClickListeners()
        film = args.film

        viewModel.seatIds.asLiveData().observe(viewLifecycleOwner) { str ->
            val seats = Gson().fromJson<ArrayList<Int>>(
                str,
                object : TypeToken<ArrayList<Int>>() {}.type
            )
            for (i in 0 until binding.layoutId.childCount) {
                val image = binding.layoutId.getChildAt(i)
                if (seats.contains(image.id)) {
                    image.isSelected = !image.isSelected
                    image.isActivated = !image.isActivated
                }
            }
        }
        viewModel.totalSeats.observe(viewLifecycleOwner, {
            binding.seatsSelected.text = HtmlCompat.fromHtml(
                getString(R.string.seats_number, it),
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
        })

        viewModel.getSeatIds().observe(viewLifecycleOwner) {
            val json = Gson().toJson(it.toList())
            viewModel.saveIDDataStore(json)

            for (i in 0 until binding.layoutId.childCount) {
                var imageView = binding.layoutId.getChildAt(i)
                if (it.contains(imageView.id)) {
                    imageView.isSelected = true
                }
            }
        }

        binding.selectedSeats.setOnClickListener {
            val seats = viewModel.totalSeats.value!!.toInt()
            viewModel.saveSeatIds()
            val action = SeatFragmentDirections.actionSeatFragmentToDateFragment(
                seats,
                film
            )
            findNavController().navigate(action)
        }
    }


    private fun getSeatIds(): ArrayList<Int> {
        var ids = ArrayList<Int>()

        lifecycleScope.launch {
            viewModel.seatIds.collectLatest { str ->
                val seats = Gson().fromJson<ArrayList<Int>>(
                    str,
                    object : TypeToken<ArrayList<Int>>() {}.type
                )
                if (seats != null) {
                    ids = seats
                }

            }
        }

        return ids

    }


    private fun setClickListeners() {
        for (i in 0 until binding.layoutId.childCount) {
            var imageView = binding.layoutId.getChildAt(i)
            imageView.setOnClickListener {
                it.isSelected = !it.isSelected
                if (it.isSelected) {
                    viewModel.addSeatsID(it.id)
                } else {
                    viewModel.addSeatsID(it.id)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}