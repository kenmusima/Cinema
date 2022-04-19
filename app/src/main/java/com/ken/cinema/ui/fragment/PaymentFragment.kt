package com.ken.cinema.ui.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.androidstudy.daraja.Daraja
import com.androidstudy.daraja.DarajaListener
import com.androidstudy.daraja.model.AccessToken
import com.androidstudy.daraja.model.LNMExpress
import com.androidstudy.daraja.model.LNMResult
import com.androidstudy.daraja.util.Env
import com.androidstudy.daraja.util.TransactionType
import com.google.firebase.messaging.FirebaseMessaging
import com.ken.cinema.BuildConfig
import com.ken.cinema.BuildConfig.CALLBACK_URL
import com.ken.cinema.R
import com.ken.cinema.data.db.entity.Ticket
import com.ken.cinema.data.model.Film
import com.ken.cinema.databinding.FragmentPaymentBinding
import com.ken.cinema.ui.viewmodel.PaymentViewModel
import com.ken.cinema.util.MpesaListener
import com.ken.cinema.util.convertLongToTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class PaymentFragment : Fragment(R.layout.fragment_payment),MpesaListener  {


    private val args : PaymentFragmentArgs by navArgs()
    private var seat by Delegates.notNull<Int>()
    private var date by Delegates.notNull<Long>()
    private lateinit var film: Film
    lateinit var daraja: Daraja
    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<PaymentViewModel>()

    private lateinit var ticket: Ticket

    companion object {
        lateinit var mpesaListener: MpesaListener
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("StringFormatMatches")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPaymentBinding.bind(view)
        seat = args.seat
        date = args.date
        film = args.film
        val price = 1000 * seat

        ticket = Ticket(seat,film.title,date,price,film.poster_path)

        mpesaListener = this
        daraja = Daraja.with(
            BuildConfig.CONSUMER_KEY,
            BuildConfig.CONSUMER_SECRET,
            Env.SANDBOX,
            object : DarajaListener<AccessToken> {
                override fun onResult(result: AccessToken) {
                    Log.d("Access Token",result.access_token)
                }

                override fun onError(error: String?) {
                    Log.d("Access Token Error","Error $error")
                }
            }
        )

        binding.bookingDetails.text = getString(R.string.payment_details,film.title,seat,date.convertLongToTime(),price)
        binding.payButton.setOnClickListener {

            val mobileNumber = binding.phoneNumber.editText?.text?.trim().toString()
            val lnmExpress = LNMExpress(
                BuildConfig.BUSINESS_SHORT_CODE,
                BuildConfig.PASSKEY,
                TransactionType.CustomerPayBillOnline,
                "1",
                mobileNumber,
                BuildConfig.BUSINESS_SHORT_CODE,
                mobileNumber,
                CALLBACK_URL,
                "001ABC",
                "Ticket Payment"
            )

            daraja.requestMPESAExpress(lnmExpress, object : DarajaListener<LNMResult> {
                override fun onResult(result: LNMResult) {
                    FirebaseMessaging.getInstance().subscribeToTopic(result.CheckoutRequestID.toString())
                    Log.d("Response : ",result.ResponseDescription)
                }

                override fun onError(error: String?) {
                    Log.d("Error : ","Error $error")
                }
            })

        }

    }

    override fun sendSuccessful(amount: String, phone: String, date: String, receipt: String) {
        lifecycleScope.launch {
            viewModel.saveTicket(ticket)
            Toast.makeText(activity?.applicationContext,"Payment Successful\n" +
                    "Receipt: $receipt\n" +
                    "Date: $date\n" +
                    "Phone: $phone\n" +
                    "Amount: $amount",Toast.LENGTH_LONG
            ).show()
            val action = PaymentFragmentDirections.actionPaymentFragmentToMainFragment()
            findNavController().navigate(action)
        }
    }

    override fun sendFailed(reason: String) {
        lifecycleScope.launch {
            Toast.makeText(
                activity?.applicationContext, "Payment Failed\n" +
                        "Reason: $reason"
                , Toast.LENGTH_LONG
            ).show()
        }
    }


}