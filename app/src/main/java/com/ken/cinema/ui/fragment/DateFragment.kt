package com.ken.cinema.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.ken.cinema.R
import com.ken.cinema.data.model.Film
import com.ken.cinema.databinding.FragmentDateBinding
import com.ken.cinema.util.convertLongToTime
import java.util.*
import kotlin.properties.Delegates
import kotlin.time.Duration.Companion.days

class DateFragment : Fragment(R.layout.fragment_date) {

    private var _binding: FragmentDateBinding? = null
    private val binding get() = _binding!!
    private val args: DateFragmentArgs by navArgs()
    private lateinit var film: Film
    private var seats by Delegates.notNull<Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDateBinding.bind(view)
        val end = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        end.add(Calendar.MONTH, 3)

        film = args.film
        seats = args.seat

        val validatorConstraints =
            CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now())
                .setStart(Calendar.getInstance().timeInMillis)
                .setEnd(end.timeInMillis)

        val materialDateBuilder = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Movie Night")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(validatorConstraints.build())
            .build()

        binding.datePicker.setOnClickListener {
            materialDateBuilder.show(this@DateFragment.requireActivity().supportFragmentManager, "MATERIAL_DATE_PICKER")
        }
        materialDateBuilder.addOnPositiveButtonClickListener {
            var calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = it
            binding.datePicked.text = getString(R.string.selected_date, it.convertLongToTime())
        }
        binding.paymentButton.setOnClickListener {
            val action = DateFragmentDirections.actionDateFragmentToPaymentFragment(
                materialDateBuilder.selection!!,
                film,
                seats
            )

            findNavController().navigate(action)

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }
}