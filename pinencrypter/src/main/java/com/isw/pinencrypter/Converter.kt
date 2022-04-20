package com.isw.pinencrypter

object Converter {
    val keyUtils = KeyUtils()
    fun GetPinBlock(ipek: String, ksn: String, plainpin: String, pan: String): String {
        val workingKey = keyUtils.getWorkingKey(IPEK = ipek, KSN = ksn)
        val pinBlock = keyUtils.DesEncryptDukpt( workingKey = workingKey, pan = pan, clearPin = plainpin )
        println("****************The expected value of the pinblock is: $pinBlock")
        return pinBlock
    }
}