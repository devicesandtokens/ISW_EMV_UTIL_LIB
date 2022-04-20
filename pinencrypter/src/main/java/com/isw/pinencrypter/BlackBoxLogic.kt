package com.isw.pinencrypter

import com.isw.pinencrypter.Utils.desEncrypt

class BlackBoxLogic {

    val xorAndorOrFunction  = XORorANDorOR()
    // the blackbox logic is tweaked to accept 16 bytes IPEK and KSN

    fun BlackBoxLogic(ksn: String, iPek: String): String {
        if (iPek.length < 32) {
            println("The expected value IPEK $iPek and IKSN is $ksn")
            val msg = xorAndorOrFunction.XORorANDorORfunction(iPek, ksn, "^")
            println("The expected value of the msg is $msg")
            val desreslt = desEncrypt(msg, iPek)
            println("The expected value of the desresult is $desreslt")
            val rsesskey = xorAndorOrFunction.XORorANDorORfunction(desreslt, iPek, "^")
            println("The expected value of the session key during BBL is $rsesskey")
            return rsesskey
        }

        val current_sk = iPek
        val ksn_mod = ksn
        val leftIpek = xorAndorOrFunction.XORorANDorORfunction( current_sk,
            "FFFFFFFFFFFFFFFF0000000000000000", "&" ).substring(16)
        val rightIpek = xorAndorOrFunction.XORorANDorORfunction(current_sk,
            "0000000000000000FFFFFFFFFFFFFFFF", "&").substring(16)
        val message = xorAndorOrFunction.XORorANDorORfunction(rightIpek, ksn_mod, "^")
        val desresult = desEncrypt(message, leftIpek)
        val rightSessionKey = xorAndorOrFunction.XORorANDorORfunction(desresult, rightIpek, "^")
        val resultCurrent_sk = xorAndorOrFunction.XORorANDorORfunction(current_sk,
            "C0C0C0C000000000C0C0C0C000000000", "^")
        val leftIpek2 = xorAndorOrFunction.XORorANDorORfunction( resultCurrent_sk,
            "FFFFFFFFFFFFFFFF0000000000000000", "&" ).substring(0, 16)
        val rightIpek2 = xorAndorOrFunction.XORorANDorORfunction( resultCurrent_sk,
            "0000000000000000FFFFFFFFFFFFFFFF", "&" ).substring(16)
        val message2 = xorAndorOrFunction.XORorANDorORfunction(rightIpek2, ksn_mod, "^")
        val desresult2 = desEncrypt(message2, leftIpek2)
        val leftSessionKey = xorAndorOrFunction.XORorANDorORfunction(desresult2, rightIpek2, "^")
        return leftSessionKey + rightSessionKey
    }

    fun encryptPinBlock(pan: String, pin: String): String {
        val pan = pan.substring(pan.length - 13).take(12).padStart(16, '0')
        println("The expected value of the encrypted pan is $pan")
        val pin = '0' + pin.length.toString(16) + pin.padEnd(16, 'F')
        println("The expected value of the clear pin is $pin")
        return xorAndorOrFunction.XORorANDorORfunction(pan, pin, "^") //the clear pinblock is returned here }
    }
}