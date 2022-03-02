package com.ken.cinema.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ken.cinema.R
import com.ken.cinema.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        auth = Firebase.auth

        if(auth.currentUser != null) {
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToMainFragment())
        }

        binding.signInButton.setOnClickListener {
            signIn()
        }

        return binding.root
    }


    private fun signIn() {
        val email = binding.signInEmail.editText?.text.toString().trim()
        val password = binding.signInPassword.editText?.text.toString().trim()

        binding.signInButton.visibility = View.INVISIBLE
        binding.progress.visibility = View.VISIBLE

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    val action = SignInFragmentDirections.actionSignInFragmentToMainFragment()
                    findNavController().navigate(action)


                } else {
                    // Sign in fails
                    binding.progress.visibility = View.INVISIBLE
                    binding.signInButton.visibility = View.VISIBLE
                    Toast.makeText(activity?.applicationContext, "Incorrect password or email.", Toast.LENGTH_SHORT)
                        .show()
                }
            }.addOnFailureListener {
                Snackbar.make(binding.root,it.message.toString(),Snackbar.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}