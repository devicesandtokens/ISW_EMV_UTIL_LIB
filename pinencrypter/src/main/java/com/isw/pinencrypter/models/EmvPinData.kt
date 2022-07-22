package com.isw.iswkozen.core.data.models

import com.isw.pinencrypter.utilsData.Constants.ISW_DUKPT_KSN

data class EmvPinData (
    var ksn : String = ISW_DUKPT_KSN,
    var CardPinBlock: String = "")