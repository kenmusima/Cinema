package com.ken.cinema.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ken.cinema.R
import com.ken.cinema.databinding.FragmentMainBinding
import com.ken.cinema.ui.adapter.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container,false)

        auth = Firebase.auth
        val pagerAdapter = ViewPagerAdapter(
            requireActivity().supportFragmentManager, viewLifecycleOwner.lifecycle
        )

        binding.toolbar.inflateMenu(R.menu.menu)
        binding.toolbar.setOnMenuItemClickListener { item ->
            if (item?.itemId == R.id.logOut) {
                auth.signOut()
                val action = MainFragmentDirections.actionMainFragmentToSignInFragment()
                findNavController().navigate(action)
            }
            false
        }

        // The pager adapter provides the pages to the view pager widget.
        binding.pager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Movies"
                }
                1 -> {
                    tab.text = "Contact"
                }
                2 -> {
                    tab.text = "Bookings"
                }
            }
        }.attach()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}