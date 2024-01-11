package com.example.biblioteczka

import android.telephony.SmsManager

class Sms {

    fun sendSms(message: String, phoneNumber: String) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
    }
}