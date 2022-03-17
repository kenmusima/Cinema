package com.ken.cinema.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.ken.cinema.data.model.MpesaResponse
import com.ken.cinema.ui.fragment.PaymentFragment

class FirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val payload = remoteMessage.data["payload"]
        val gson = Gson()

        val mpesaResponse = gson.fromJson(payload,MpesaResponse::class.java)
        var id = mpesaResponse.Body.stkCallback.CheckoutRequestID
        if (mpesaResponse.Body.stkCallback.ResultCode != 0) {
            var reason = mpesaResponse.Body.stkCallback.ResultDescription
            PaymentFragment.mpesaListener.sendFailed(reason)
        } else {
            val list = mpesaResponse.Body.stkCallback.CallbackMetadata.Item
            var receipt = ""
            var date = ""
            var phone = ""
            var amount = ""

            for (item in list){

                if (item.Name == "MpesaReceiptNumber") {
                    receipt = item.Value
                }
                if (item.Name == "TransactionDate") {
                    date = item.Value
                }
                if (item.Name == "PhoneNumber") {
                    phone = item.Value

                }
                if (item.Name == "Amount") {
                    amount = item.Value
                }
            }
            PaymentFragment.mpesaListener.sendSuccessful(amount, phone, date, receipt)
        }
        FirebaseMessaging.getInstance().unsubscribeFromTopic(id)
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

    }
}