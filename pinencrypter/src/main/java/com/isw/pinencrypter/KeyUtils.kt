package com.isw.pinencrypter

import com.isw.pinencrypter.Utils.byteArrayToHexString
import com.isw.pinencrypter.Utils.hexStringToByteArray
import java.io.ByteArrayOutputStream
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

class KeyUtils {

    val xorAndorOrFunction  = XORorANDorOR()


    fun getWorkingKey(
        IPEK: String = "9F8011E7E71E483B",
        KSN: String = "0000000006DDDDE01500"
    ): String {
        var initialIPEK: String =
            IPEK
        println ("The expected value of the initial IPEK $initialIPEK")
        val ksn = KSN.padStart(20, '0')
        println ("The expected value of the ksn $ksn")
        var sessionkey = ""
            //Get ksn with a zero counter by ANDing it with 0000FFFFFFFFFFE00000
        val newKSN = xorAndorOrFunction.XORorANDorORfunction(
            ksn,
            "0000FFFFFFFFFFE00000",
            "&"
        )
        println ("The expected value of the new KSN is $newKSN")
        val counterKSN = ksn.substring(ksn.length - 5).padStart(
            16,
            '0'
        )
        println ("The expected value of the counter KSN is $counterKSN")
        //get the number of binary associated with the counterKSN number
        var newKSNtoleft16 = newKSN.substring(newKSN.length - 16)
        println("The expected value of the new KSN to left 16 $newKSNtoleft16")
        val counterKSNbin = Integer.toBinaryString(counterKSN.toInt())
        println("The expected value of the counter KSN Bin $counterKSNbin")
        var binarycount = counterKSNbin
        for (i in 0 until counterKSNbin.length) {
            val len: Int = binarycount.length
            var result = ""
            if (binarycount.substring(0, 1) == "1") {
                result = "1".padEnd(len, '0')
            println("The expected value of the result is $result")
            binarycount = binarycount.substring(1)
            println("The value of the new binary count is $binarycount")
            } else
            { binarycount = binarycount.substring(1)
                println("The value of the new binary count is $binarycount")
                continue
            }
            val counterKSN2 = Integer.toHexString(Integer.parseInt(
                result, 2)) .toUpperCase().padStart(16, '0')
            println("The expected value of the counter ksn 2 is $counterKSN2")
            val newKSN2 = xorAndorOrFunction.XORorANDorORfunction(
                newKSNtoleft16, counterKSN2, "|")
            println("The expected value of the new ksn 2 is $newKSN2")
            sessionkey = BlackBoxLogic().BlackBoxLogic(newKSN2, initialIPEK)
            //Call Blackbox here
            println("The expected value of the session key here is $sessionkey")
            newKSNtoleft16 = newKSN2
            initialIPEK = sessionkey

            println("The expected value of the IPEK  here is $initialIPEK")
           }
        val checkWorkingKey = xorAndorOrFunction.XORorANDorORfunction(
            sessionkey, "00000000000000FF00000000000000FF", "^" )
        println("**********The value of the working key is $checkWorkingKey")
        return xorAndorOrFunction.XORorANDorORfunction(
            sessionkey,"00000000000000FF00000000000000FF","^")
    }


    fun DesEncryptDukpt(workingKey: String, pan: String, clearPin: String): String {
        val pinBlock = xorAndorOrFunction.XORorANDorORfunction(workingKey,
            BlackBoxLogic().encryptPinBlock(pan, clearPin), "^")
        val keyData = hexStringToByteArray(workingKey)
        val bout = ByteArrayOutputStream()
        try {
            val keySpec: KeySpec = DESKeySpec(keyData)
            val key: SecretKey = SecretKeyFactory.getInstance("DES").generateSecret(keySpec)
            val cipher: Cipher = Cipher.getInstance("DES/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, key)
            bout.write(cipher.doFinal(hexStringToByteArray(pinBlock))) //DES Encryption
         } catch (e: Exception) {
             println("Exception .. " + e.message)
        }

     return xorAndorOrFunction.XORorANDorORfunction( workingKey,
         byteArrayToHexString(bout.toByteArray()).substring( 0, 16 ), "^" )

    }
}