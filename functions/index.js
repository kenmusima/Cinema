const functions = require('firebase-functions');
const admin = require('firebase-admin');
const express = require('express');
const bodyParser = require('body-parser');

admin.initializeApp();

const app = express()
app.use(bodyParser.json())

app.post('/mpesaCallbackUrl', (req, res) => {
    let response = {
        "ResultCode": 0,
        "ResultDesc": "Success"
    }
    //payload has been received successfully
    res.status(200).json(response);

      //Then handle data through above received payload as per your app logic.
    let body = req.body;
    let payload = JSON.stringify(body)

      console.log(payload)

    let id =  body.Body.stkCallback.CheckoutRequestID

      const payloadSend = {
            data: {
                payload,
            },
             topic: id
        };

         return admin.messaging().send(payloadSend).catch(error=>{
         console.error(error)
         })


})

exports.api = functions.https.onRequest(app);