package com.example.room.mvvm.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi


class CommonUtils {
}

@SuppressLint("MissingPermission")
fun Context.getIMEI(): String {

    var imei = ""


    try {
        val telephonyManager =
            getSystemService(android.content.Context.TELEPHONY_SERVICE) as android.telephony.TelephonyManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            imei = telephonyManager.imei ?: ""
        } else {
            imei =  telephonyManager.deviceId ?: ""
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return imei
}

@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
fun Context.getIMSI(): String {

    var imsi = ""

    try {
        val telephonyManager =
            getSystemService(android.content.Context.TELEPHONY_SERVICE) as android.telephony.TelephonyManager

        imsi = telephonyManager.subscriberId
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return imsi
}