package com.saher.nexgo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.nexgo.oaf.apiv3.APIProxy
import com.nexgo.oaf.apiv3.SdkResult
import com.nexgo.oaf.apiv3.device.printer.AlignEnum
import com.nexgo.oaf.apiv3.device.printer.OnPrintListener
import com.saher.nexgo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnPrintListener {
    val binding: ActivityMainBinding
    get() = DataBindingUtil.setContentView(this, R.layout.activity_main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val engine = APIProxy.getDeviceEngine(this)
        val printer = engine.printer
        printer.initPrinter()
        binding.button.setOnClickListener {
            printer.appendQRcode("Sample QR Code for the PrintSample Application!",384, 7, 3, AlignEnum.CENTER)
            printer.startPrint(true, this)
        }
        when (printer.status) {
            SdkResult.Success -> binding.printerStatus.text = "Found Printer"
            SdkResult.Printer_NoDevice -> binding.printerStatus.text = "Didn't find Printer"
            SdkResult.Printer_Fault -> binding.printerStatus.text = "Printer Fault"
            SdkResult.Fail -> binding.printerStatus.text = "Printer Failt"
        }
    }

    override fun onPrintResult(resultCode: Int) {
        when (resultCode) {
            SdkResult.Success -> binding.printerStatus.text = "Printer job finished successfully!"
            SdkResult.Printer_Print_Fail -> binding.printerStatus.text ="Printer Failed: $resultCode"
            SdkResult.Printer_Busy -> binding.printerStatus.text = "Printer is Busy: $resultCode"
            SdkResult.Printer_PaperLack -> binding.printerStatus.text = "Printer is out of paper: $resultCode"
            SdkResult.Printer_Fault -> binding.printerStatus.text = "Printer fault: $resultCode"
            SdkResult.Printer_TooHot -> binding.printerStatus.text ="Printer temperature is too hot: $resultCode"
            SdkResult.Printer_UnFinished -> binding.printerStatus.text = "Printer job is unfinished: $resultCode"
            SdkResult.Printer_Other_Error -> binding.printerStatus.text = "Printer Other_Error: $resultCode"
            else -> binding.printerStatus.text = "Generic Fail Error: $resultCode"
        }
    }
}