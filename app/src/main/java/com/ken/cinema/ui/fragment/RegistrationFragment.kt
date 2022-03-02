package com.ken.cinema.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ken.cinema.databinding.FragmentRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private lateinit var auth: FirebaseAuth

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        binding.register.setOnClickListener {
            val (emailValid, passwordValid) = validateFields()
            createUser(emailValid, passwordValid)
        }

        binding.goToLogin.setOnClickListener {
            val action = RegistrationFragmentDirections.actionRegistrationFragmentToSignInFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }


    private fun validateFields(): Pair<Boolean, Boolean> {
        val email = binding.emailRegistration.text.toString().trim()
        val password = binding.passwordRegistration.text.toString().trim()
        val confirmPassword = binding.confirmPasswordRegistration.text.toString().trim()

        val isEmailValid: Boolean = if (TextUtils.isEmpty(email)) {
            Toast.makeText(activity, "Please enter email", Toast.LENGTH_SHORT).show()
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(activity, "Invalid Email Address", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }

        val isPasswordValid: Boolean = when {
            TextUtils.isEmpty(password) -> {
                Toast.makeText(activity, "Please enter a password", Toast.LENGTH_SHORT).show()
                false
            }
            password.length < 6 -> {
                Toast.makeText(activity, "Password length should be greater than 6", Toast.LENGTH_SHORT)
                    .show()
                false
            }
            password != confirmPassword -> {
                Toast.makeText(activity, "Password don't match", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }

        return Pair(isEmailValid, isPasswordValid)

    }

    private fun createUser(emailValid: Boolean, passwordValid: Boolean) {

        if (emailValid && passwordValid) {
            val email = binding.emailRegistration.text.toString().trim()
            val password = binding.passwordRegistration.text.toString().trim()

            binding.progressCircular.visibility = View.VISIBLE
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success
                        val action = RegistrationFragmentDirections.actionRegistrationFragmentToSignInFragment()
                        findNavController().navigate(action)

                    } else {
                        // Sign in fails
                        binding.progressCircular.visibility = View.GONE
                        Toast.makeText(activity?.applicationContext, "User Registration Failed.", Toast.LENGTH_SHORT)
                            .show()

                    }
                }
                .addOnFailureListener {
                    Snackbar.make(binding.root,it.message.toString(),Snackbar.LENGTH_SHORT).show()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}