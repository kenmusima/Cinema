package com.ken.cinema.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ken.cinema.R
import com.ken.cinema.databinding.FragmentContactBinding
import com.ken.cinema.util.FIRESTORE_STORAGE
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ContactFragment : Fragment(R.layout.fragment_contact) {


    private var _binding: FragmentContactBinding? = null
    private val binding get() = _binding!!
    private var map: GoogleMap? = null

    private val location = LatLng(-1.284134344050467, 36.825352535875055)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentContactBinding.bind(view)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync {
            map = it
            map!!.uiSettings.isMapToolbarEnabled = true
            map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15F))
            map!!.addCircle(
                CircleOptions().center(location).radius(30.0).strokeWidth(2.0F)
                    .fillColor(Color.GRAY).strokeColor(Color.BLACK)
            )
            map!!.addMarker(
                MarkerOptions()
                    .position(location)
                    .title("Cinema CBD")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

            )

        }

        binding.submit.setOnClickListener {
            submitFeedback()
        }
    }

    private fun submitFeedback() {
        val feedBackText = binding.feedbackText.editText?.text?.trim().toString()
        val optionalContact = binding.contactDetail.editText?.text?.trim().toString()
        val userEmail = Firebase.auth.currentUser!!.email
        val userRating = binding.rating.rating
        val db = Firebase.firestore

        if (feedBackText.isEmpty()) {
            binding.feedbackText.isErrorEnabled = true
            binding.feedbackText.error = resources.getString(R.string.feedback_error)
        } else {
            val userFeedback = hashMapOf(
                "feedback" to feedBackText,
                "user" to userEmail,
                "contact" to optionalContact,
                "rating" to userRating
            )
            db.collection("feedbacks")
                .add(userFeedback)
                .addOnSuccessListener {
                    Toast.makeText(activity?.applicationContext,"Feedback is Highly Appreciated.",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Log.e(FIRESTORE_STORAGE,"Error saving document ${exception.localizedMessage}")
                }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.mapView?.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView?.onSaveInstanceState(outState)
    }


}
