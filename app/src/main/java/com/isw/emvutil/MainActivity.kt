package com.isw.emvutil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.isw.pinencrypter.Converter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkLogic()
    }


    /*
    * The Ksn here is the ksn with the key counter*/
    private fun checkLogic() {
        val block  = Converter.GetPinBlock("3F2216D8297BCE9C",
            "0000000002DDDDE00002", "1994", "4105400015916727")

        println("this is the pin block oo => $block")

    }

}