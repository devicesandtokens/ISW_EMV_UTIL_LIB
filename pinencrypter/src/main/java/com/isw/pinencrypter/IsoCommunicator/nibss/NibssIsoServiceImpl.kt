package com.isw.pinencrypter.IsoCommunicator.nibss

import android.content.Context
import com.isw.iswkozen.core.data.utilsData.AccountType
import com.isw.pinencrypter.utilsData.Constants
import com.isw.pinencrypter.utilsData.Constants.KEY_PIN_KEY
import com.isw.pinencrypter.utilsData.Constants.KEY_SESSION_KEY
import com.isw.pinencrypter.utilsData.RequestIccData
import com.isw.pinencrypter.IsoCommunicator.IsoSocket
import com.isw.pinencrypter.IsoCommunicator.nibss.builders.IsoTransactionBuilder
import com.isw.iswkozen.core.network.models.PurchaseResponse
import com.isw.pinencrypter.models.TerminalInfo
import com.isw.pinencrypter.utilsData.KeysUtils
import com.pixplicity.easyprefs.library.Prefs

/**
 *
 * @author ayooluwa.olosunde
 */

class NibssIsoServiceImpl(
    private val context: Context,
    private val socket: IsoSocket,
    val isoTransactionBuilder: IsoTransactionBuilder,
    val keyCallback: ( messageData: String) -> String
){

    suspend fun downloadKey(terminalId: String,
                            ip: String,
                            port: Int,
                            cms: String,
                            TMK: String,
                            TSK: String,
                            TPK: String
                            ): Boolean {

        println("cms: $cms")
        println("ip  => $ip  port => $port")

        // getResult master key & save
        val isDownloaded = isoTransactionBuilder.KeyTransactionBuilder(terminalId, ip, port, TMK, cms)?.let { masterKey ->
            Prefs.putString(Constants.KEY_MASTER_KEY, masterKey)
            // load master key into pos
            println("masterKey ::: ${masterKey}")
            keyCallback("masterkey:$masterKey")

            // getResult pin key & save
            val isSessionSaved =
                isoTransactionBuilder.KeyTransactionBuilder(terminalId, ip, port, TSK, masterKey)?.let { sessionKey ->
                    Prefs.putString(KEY_SESSION_KEY, sessionKey)
                    true
                }

            // getResult pin key & save
            val isPinSaved = isoTransactionBuilder.KeyTransactionBuilder(terminalId, ip, port, TPK, masterKey)?.let { pinKey ->
                Prefs.putString(KEY_PIN_KEY, pinKey)

                // load pin key into pos device

                println("pinkey ::: ${pinKey}")
                keyCallback("pinkey:$pinKey")
                true
            }

            isPinSaved == true && isSessionSaved == true
        }

        return isDownloaded == true
    }

    suspend fun downloadTerminalDetails(
        terminalId: String,
        ip: String,
        port: Int,
        processingCode: String,
    ): TerminalInfo? {
        println("ip  => $ip  port => $port")
        val terminalData = isoTransactionBuilder.parameterTransactionBuilder(
            terminalId,
            ip,
            port,
            processingCode)

        return terminalData
    }

    suspend fun get200Request(
        ip: String,
        port: Int,
        terminalData: TerminalInfo,
        iccData: RequestIccData,
        accountType: AccountType
    ): PurchaseResponse {
        val responseIso = isoTransactionBuilder.get200Request(
            ip,
            port,
            terminalData,
            iccData,
            accountType)

        return responseIso
    }


    suspend fun getPaycodeRequest(
        ip: String,
        port: Int,
        code: String,
        terminalData: TerminalInfo,
       amount: Long
    ): PurchaseResponse {
        val responseIso = isoTransactionBuilder.getPaycodePurchase(
            terminalData,
            code,
            amount.toString(),
            ip,
            port,)

        return responseIso
    }

}