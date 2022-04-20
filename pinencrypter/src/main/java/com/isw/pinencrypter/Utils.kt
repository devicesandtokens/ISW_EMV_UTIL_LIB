package com.isw.pinencrypter

import java.io.ByteArrayOutputStream
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

object Utils {
    fun hexStringToByteArray(key: String) : ByteArray {
        var result:ByteArray = ByteArray(0)
        for (i in 0 until key.length step 2) {
            result += Integer.parseInt(key.substring(i, (i + 2)), 16).toByte()
        }
        return result
    }

    fun byteArrayToHexString(key: ByteArray) : String {
        var st = ""
        for (b in key) {
            st += String.format("%02X", b)
        }
        return st
    }

    fun desEncrypt(desData: String, key: String): String {
        val keyData = hexStringToByteArray(key)
        val bout = ByteArrayOutputStream()
        try {
            val keySpec: KeySpec = DESKeySpec(keyData)
            val key: SecretKey = SecretKeyFactory
                .getInstance("DES").generateSecret(keySpec)
            val cipher: Cipher = Cipher.getInstance("DES/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, key)
            bout.write(cipher.doFinal(hexStringToByteArray(desData)))
        } catch (e: Exception) {
            print("Exception DES Encryption.. " + e.printStackTrace())
        }
        return byteArrayToHexString(bout.toByteArray()).substring(0, 16)
    }


}