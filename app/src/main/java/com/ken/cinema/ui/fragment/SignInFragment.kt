package com.ken.cinema.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ken.cinema.R
import com.ken.cinema.databinding.FragmentSignInBinding

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        binding.signInButton.setOnClickListener {
            signIn()
        }

        return binding.root
    }


    private fun signIn() {

        var email = binding.signInEmail.text.toString().trim()
        var password = binding.signInPassword.text.toString().trim()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    val action = SignInFragmentDirections.actionSignInFragmentToMainActivity()
                    findNavController().navigate(action)

                } else {
                    // Sign in fails
                    Toast.makeText(activity, "Incorrect password or email.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
}