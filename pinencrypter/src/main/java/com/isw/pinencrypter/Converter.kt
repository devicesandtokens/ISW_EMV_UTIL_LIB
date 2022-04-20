package com.isw.pinencrypter

object Converter {
    val keyUtils = KeyUtils()

    /**
     * @param ksn is the KSN with the Key counter
     * the key counter must increment per transaction
     * most times it's the same value with the STAN
     * @param ipek is gotten from your provider
     * @param plainpin is the pin for provided by the card holder
     * @param pan is the pan of the card being read by the terminal
     * */
    fun GetPinBlock(ipek: String, ksn: String, plainpin: String, pan: String): String {
        val workingKey = keyUtils.getWorkingKey(IPEK = ipek, KSN = ksn)
        val pinBlock = keyUtils.DesEncryptDukpt( workingKey = workingKey, pan = pan, clearPin = plainpin )
        println("****************The expected value of the pinblock is: $pinBlock")
        return pinBlock
    }
}